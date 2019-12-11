package Hike;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private File fileName;
    private String fileURL;
    private String fileType;    //type of file name (determines whether to use fileName or fileURL)
    private int mapNum;    //determines which map to load
    private int numOfLines;    //number of lines in file, to keep track for multimap reading
    private boolean numOfLinesHasntRun = true;    //tells if counted numOfLines yet

    //Constructor variables
    private Integer startX;
    private Integer startY;
    private Integer endX;
    private Integer endY;
    private char[][] map = new char[5][5];

    //Return Variable
    private ArrayList terrainMaps = new ArrayList<TerrainMap>();

    private void input() {
        //fileReader variables (including variables for breaking down code)
        String line;    //hold the read in line
        String[] vars = new String[2];    //temp for coordinates
        String temp;    //temp variable for stringing line
        FileReader fileReader;

        try {
            //creates buffered Reader
            if (fileType.equals("File")) {    //determines what kind of file name type to use
                fileReader = new FileReader(fileName);
            } else {
                fileReader = new FileReader(fileURL);
            }

            //counts total number of lines in file
            if (numOfLinesHasntRun) {
                BufferedReader bufferedReader2 = new BufferedReader(fileReader);
                while ((line = bufferedReader2.readLine()) != null) {
                    numOfLines++;
                }
                numOfLinesHasntRun = false;    //ensures file is only counted once
            }

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //skips to map you want to use
            for (int i = 0; i < (mapNum * 6); i++) {
                line = bufferedReader.readLine();
            }

            //coordinate reader
            line = bufferedReader.readLine();
            for (int k = 0; k < 2; k++) {    //loops for start and end coordinates
                int o = 1 + (k * 6);    //for first substring parameter
                int p = 4 + (k * 6);    //for second substring parameter
                temp = line.substring(1, line.length() - 1);    //removes curly brackets
                vars[k] = temp.substring(o, p);     //format [0,0]
            }
            startX = Integer.parseInt(vars[0].substring(0, 1));
            startY = Integer.parseInt(vars[0].substring(1, 2));
            endX = Integer.parseInt(vars[1].substring(0, 1));
            endY = Integer.parseInt(vars[1].substring(1, 2));

            //map Reader
            for (int x = 0; x < 5; x++) {   //for each line of map ex: [1,1,1,1,1]
                line = bufferedReader.readLine();
                for (int y1 = 0; y1 < 5; y1++) {    //for each char in the line
                    int y2 = y1 + 1;    //so it only substrings a single char
                    map[x][y1] = (line.substring(y1, y2)).charAt(0);    //takes a single char in
                }
            }

        } catch (
                FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (
                IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    public TerrainMap createTerrainMap() {
        input();
        return new TerrainMap(startX, startY, endX, endY, map);
    }

    public ArrayList<TerrainMap> createTerrainMaps() {
        mapNum = 0;
        for (int run = 0; run < (numOfLines / 6); run++) {                                                 //unhardcode
            input();
            TerrainMap terrainMap = new TerrainMap(startX, startY, endX, endY, map);
            terrainMaps.add(terrainMap);
            mapNum++;
        }
        return terrainMaps;
    }

    /////////////////////Constructors///////////////////
    public FileHandler(File fileName, int mapNum) {
        this.mapNum = mapNum;
        this.fileName = fileName;
        this.fileType = "File";
    }

    public FileHandler(String fileURL, int mapNum) {
        this.mapNum = mapNum;
        this.fileURL = fileURL;
        this.fileType = "String";
    }

    public FileHandler(File fileName) {
        this.fileName = fileName;
        this.fileType = "File";
    }

    public FileHandler(String fileURL) {
        this.fileURL = fileURL;
        this.fileType = "String";
    }
}
