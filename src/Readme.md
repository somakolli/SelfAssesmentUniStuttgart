## Build & Run

To Build from source Maven is required.

Build with `mvn package` and execute `java -jar target/SelfAssesment-1.0-jar-with-dependencies.jar` 
to run the GUI.

If you don't want to build yourself you can find a pre build jar in the bin folder. You can also run that with `java -jar bin/SelfAssesment-1.0-jar-with-dependencies.jar`

A demo-test is located in `bin/demo-test`. To use it just got to `File -> Import XML` and choose the `demo-test.xml`.
For the images to be shown correctly chose `Insert Media -> Set Media Folder` and chose the `demo-images` folder.

To then Generate the Website chose `File -> Generate Website` which will generate a zip with the chosen location and name.

To Generate a Website using the command line run `java -jar SelfAssesment-1.0-jar-with-dependencies.jar pathToXml pathToMediaFolder`. The website will the be safed as `website.zip` in the folder you ran the command.

So to Generate a website using the demo-test you can just run `java -jar bin/SelfAssesment-1.0-jar-with-dependencies.jar bin/demo-test/demo-test.xml bin/demo-test/demo-images/`.

## Documentation

You can find a generated JavaDoc and JavaScriptDoc in the `bin` folder.

## GUI Guide

### Import and Export XML
To write a Self Assesment Test you can either write one from scratch or import 
an existing xml with `File -> Import xml`.
To export your current Progress you can chose `File -> Export XML`.

### Categories
To add a new category, select File -> New Category and input the Category name.
Categories must have unique names.

### Questions
To add a question first select the category and the select `File -> New Question` or 
you can right click on the category and select New Question.
In the center left window you can edit the questions content and in the center right window you
can preview how the content will look once the website is generated.
In the bottom you can change the time, the points and if the question is single choice or not.

### Answers
To add an answer you need to select the question and either `right click -> New Answer` or `File -> New Answer`.
In the bottom you can set the answer to correct or incorrect.

### Conclusions
Too add a conclusion, choose `File -> new Conclusion`.
The conclusion will be displayed in the end of the generated Test. 
You can add multiple conclusions.
Based on the achieved points one of the conclusions will be displayed.
If you have selected a conclusion you can edit the point range in the bottom.

Lets assume for example that you have one conlusion with the range 10 and one with the range 20.
If the points achieved range from 0 - 10 the conclusion with range 10 will be shown.
If the points achieved range from 11 - 20 the conclustion with range 20 will be shown.

### Media
You can either add Images or Videos with `Insert Media -> Image` or `Insert Media -> Video`.
The HTML text will be added to your currently viewed item.
You can set the source in the src tag.

You can either input absolute sources but if you want to import your test on another 
computer you should setthe media folder with `Insert Media -> Set Media Folder`.
Then you have to input the sources relative to that path.

Lets assume you have a folder `../Desktop/media` where you have an image `image.png`.

If you set that folder as your media folder you just need to enter `image.png` as the source.

If you then import that test on another computer and `image.png` is stored in `../Pictures`
then you can select `../Pictures` as your media folder and the images should show correctly 
without changing the question.

## Generating a Website

To generate a website you have to select `File -> Generate Website` and select a folder
and chose a name for the zip file.

To deploy the website unpack the zip file and serve the files in it with a http-webserver.
You need a server to correctly test the website, just opening the index.html on a browser
will not work.

## Markdown
You can use markdown or html to edit the contents of questions, answers or conclusions.

For Markdown the library [atlassian-commonmark-java](https://github.com/atlassian/commonmark-java) is used.

You can see the possible markdown commands here https://commonmark.org/.

To input videos or images please use html, since markdown does not allow to change the size.

If you want to center the content use `<center></center>`




