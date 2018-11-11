import creator.TextEditor;
import generator.VGenerator;
import javafx.application.Application;
import parser.Parser;

import java.io.File;

public class Main extends TextEditor {
    public static void main(String[] args) {
        if(args.length == 0){
            Main.launch();
        } else {
            Parser parser = new Parser();
            parser.setFile(new File(args[0]));
            parser.startParser();
            VGenerator vGenerator = new VGenerator();
            vGenerator.setMediaPath(args[1]);
            vGenerator.createZipArchive(parser.getRootelement(), "website.zip");
            System.exit(0);
        }
    }
}
