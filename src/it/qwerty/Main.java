package it.qwerty;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;


public class Main {

    public static void main(String[] args) {
        if(args.length == 2){
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export3.dtd\">\n" +
                    "<en-export export-date=\"20130730T205637Z\" application=\"Evernote\" version=\"Evernote Mac\">");

            try {
                Document document = Jsoup.parse(new File(args[0]), "UTF-8");

                Elements elements = document.select("a");
                int i = 1;
                for (Element el : elements) {
                    Note note = parseNote(el);

                    constructEnex(sb, note);
                }
                sb.append("</en-export>");
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(args[1]), "UTF-8"));
                    writer.write(sb.toString());
                } finally {
                    if (writer != null) writer.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            System.out.println("Invalid argument\n Usage: BookmarksToEvernote <pathToBookmarks> <EnexFilePath>");
        }
    }

    private static void constructEnex(StringBuilder sb, Note note) {
        sb.append("<note><title>" + note.getTitle() + "</title>");

        sb.append("<content><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "                <!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n" +
                "                <en-note style=\"word-wrap: break-word; -webkit-nbsp-mode: space; -webkit-line-break: after-white-space;\">\n");


        String resource = "";
        if(note.getResourceBase64() != null ){
            sb.append(" <div> <en-media alt=\"\" type=\"image/png\" hash=\""+note.getResourceMD5()+"\"/> </div>"+ "<div>"+note.getHref()+"</div>") ;
            resource = "<resource>\n" +
                    "            <data encoding=\"base64\">" + note.getResourceBase64()+ "</data>"+
                    "            <mime>"+note.getResourceMimeType()+"</mime>\n" +
                    "            <width>1280</width>\n" +
                    "            <height>720</height>\n" +
                    "            <resource-attributes>\n" +
                    "                <file-name>ico-"+note.getResourceMD5()+".png</file-name>\n" +
                    "            </resource-attributes>\n" +
                    "        </resource>";

        }
        sb.append("\n" + "</en-note>\n" +"]]>\n</content>");
        sb.append("<created>" + note.getCreationDate() + "</created>");
        sb.append("<tag>"+note.getTag()+"</tag>");

        if(!resource.isEmpty()){
            sb.append(resource);
        }
        sb.append("</note>");


    }


    static Note parseNote(Element el){
        Note resultNote =  new Note();
        resultNote.setTitle("Empty");
        if (!el.text().isEmpty()){
            String escaped = StringEscapeUtils.escapeHtml4(el.text());
            if (escaped.length() > 250) { // title must be 250 chars max
                resultNote.setTitle(escaped.subSequence(0, 249).toString());
            }else{
                resultNote.setTitle(escaped);
            }
        }
        String md5 = null;
        String base64 = null;
        String mime = null;

        String iconAttr = el.attr("ICON");
        if(!iconAttr.isEmpty()) {
            String[] icon = iconAttr.split(",");
            mime =  icon[0].split(":")[1].split(";")[0];
            byte[] imageBytes = Base64.decodeBase64(icon[1]);
            base64 = icon[1];
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(imageBytes);
        }
        resultNote.setResourceMD5(md5);
        resultNote.setResourceBase64(base64);
        resultNote.setResourceMimeType(mime);

        String href = el.attr("href");
        if(!href.isEmpty()) {
            resultNote.setHref(href);
        }
        String creationDate = el.attr("ADD_DATE");
        if(!creationDate.isEmpty()) {
            resultNote.setCreationDate(creationDate);
        }
        String tag = el.parent().parent().parent().child(0).text();
        if(!tag.isEmpty()) {
            resultNote.setTag(StringEscapeUtils.escapeHtml4(tag));
        }
        return resultNote;
    }
}