import creator.TextEditor;
import generator.VGenerator;
import parser.Parser;

import java.io.File;

public class Main extends TextEditor{
    public static void main(String[] args) {
        if(args.length == 0){
            TextEditor.launch(args);
        } else {
            Parser parser = new Parser();
            parser.setFile(new File(args[0]));
            parser.startParser();
            VGenerator vGenerator = new VGenerator();
            vGenerator.createZipArchive(parser.getRootelement(), "website.zip");
        }
    }
}
