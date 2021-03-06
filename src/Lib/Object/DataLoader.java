package Lib.Object;

import Lib.Entities.Data;
import Lib.Entities.Structure;
import Form.HouseCalc;
import Lib.Graphics.*;
import Mapper.Data.D;
import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class DataLoader {
    
    private static ArrayList<Tex> textures = new ArrayList<>();
    private static ArrayList<ObjectData> models = new ArrayList<>();
    private static ArrayList<Materials> materials = new ArrayList<>();
    
    public static DefaultListModel grounds = new DefaultListModel();
    public static DefaultListModel caveGrounds = new DefaultListModel();
    private static DefaultListModel floors = new DefaultListModel();
    private static DefaultListModel walls = new DefaultListModel();
    private static DefaultListModel roofs = new DefaultListModel();
    private static DefaultListModel objects = new DefaultListModel();
    
    private static final TrueTypeFont font = new TrueTypeFont(Font.decode("Arial 12"), true);
    
    private static Tex getTex(int id) {
        for (Tex tex : textures) {
            if (tex.loadID==id) {
                return tex;
            }
        }
        return null;
    }
    
    private static ObjectData getModel(int id) {
        for (ObjectData obj : models) {
            if (obj.loadID==id) {
                return obj;
            }
        }
        return null;
    }
    
    private static Materials getMaterials(int id) {
        for (Materials m : materials) {
            if (m.getID()==id) {
                return m;
            }
        }
        return null;
    }
    
    private static String spaceMax(String temp) {
        if (temp.indexOf(" ")>=0) {
            return temp.substring(0, temp.indexOf(" "));
        }
        return temp.substring(0, temp.length());
    }
    
    public static void load() throws FileNotFoundException, IOException {
        String slash = System.getProperty("file.separator");
        
        File list = new File("Data"+slash+"Objects.txt");
        if (list.exists()) {
            preloader(list);
            specials();
        }
        else {
            System.exit(1);
        }
    }
    
    private static void preloader(File list) throws FileNotFoundException, IOException {
        int allCommands = countCommands(list);
        int currentCommand = 0;
        
        floors.addElement("Delete");
        walls.addElement("Delete");
        roofs.addElement("Delete");
        objects.addElement("Delete");
        
        String slash = System.getProperty("file.separator");
        
        String temp;
        String name;
        String shortName;
        int tid;
        int mid;
        int matid;
        Data data;
        Structure structure;
        Ground ground;
        
        long totalTime = System.currentTimeMillis();
        
        BufferedReader reader = new BufferedReader(new FileReader(list));
        String line;
        while ((line=reader.readLine())!=null) {
            if (!line.equals("")) {
                currentCommand++;
                System.out.println("Processing line "+currentCommand+"/"+allCommands+": "+line);
                String request = line.substring(0, line.indexOf(" "));
                switch (request) {
                    case "Texture":
                        Tex tex = new Tex("Data"+slash+"Textures"+slash+line.substring(line.indexOf("source=\"")+8, line.lastIndexOf("\"")));
                        temp = line.substring(line.indexOf("id=")+3);
                        tid = Integer.parseInt(spaceMax(temp));
                        tex.loadID = tid;
                        textures.add(tex);
                        break;
                    case "Model":
                        String source = "Data"+slash+"Models"+slash+line.substring(line.indexOf("source=\"")+8, line.lastIndexOf("\""));
                        temp = line.substring(line.indexOf("id=")+3);
                        mid = Integer.parseInt(spaceMax(temp));
                        ObjectData object = new ObjectData(mid, source);
                        models.add(object);
                        break;
                    case "Materials":
                        line = line.substring(line.indexOf(" ")+1);
                        matid = Integer.parseInt(line.substring(line.indexOf("id=")+3, line.indexOf(" ")));
                        
                        Materials mats = new Materials(matid);
                        
                        while (true) {
                            if (line.indexOf(" ")!=-1) {
                                line = line.substring(line.indexOf(' ')+1);
                                if (line.isEmpty()) {
                                    break;
                                }
                            }
                            else {
                                break;
                            }
                            temp = spaceMax(line);
                            mats.put(temp.substring(0, temp.indexOf("=")).replace('_', ' '), Integer.parseInt(temp.substring(temp.indexOf("=")+1)));
                        }
                        materials.add(mats);
                        break;
                    case "Wall":
                        temp = line.substring(line.indexOf("tid=")+4);
                        tid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("mid=")+4);
                        mid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("matid=")+6);
                        matid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("name=\"")+6);
                        name = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("shortname=\"")+11);
                        shortName = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("r=")+2);
                        double r = Double.parseDouble(spaceMax(temp));
                        temp = line.substring(line.indexOf("g=")+2);
                        double g = Double.parseDouble(spaceMax(temp));
                        temp = line.substring(line.indexOf("b=")+2);
                        double b = Double.parseDouble(spaceMax(temp));
                        temp = line.substring(line.indexOf("mult=")+5);
                        float rendMult = Float.parseFloat(spaceMax(temp));
                        data = new Data(name, shortName, Type.wall, getTex(tid), getModel(mid));
                        data.materials = getMaterials(matid);
                        data.r = r;
                        data.g = g;
                        data.b = b;
                        data.renderMultiplier = rendMult;
                        walls.addElement(data);
                        D.models.add(data);
                        break;
                    case "Floor":
                        temp = line.substring(line.indexOf("tid=")+4);
                        tid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("mid=")+4);
                        mid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("matid=")+6);
                        matid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("name=\"")+6);
                        name = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("shortname=\"")+11);
                        shortName = temp.substring(0, temp.indexOf("\""));
                        data = new Data(name, shortName, Type.floor, getTex(tid), getModel(mid));
                        data.materials = getMaterials(matid);
                        floors.addElement(data);
                        D.models.add(data);
                        break;
                    case "Roof":
                        temp = line.substring(line.indexOf("tid=")+4);
                        tid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("matid=")+6);
                        matid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("name=\"")+6);
                        name = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("shortname=\"")+11);
                        shortName = temp.substring(0, temp.indexOf("\""));
                        data = new Data(name, shortName, Type.roof, getTex(tid), null);
                        data.materials = getMaterials(matid);
                        roofs.addElement(data);
                        D.models.add(data);
                        break;
                    case "Ground":
                        temp = line.substring(line.indexOf("tid=")+4);
                        tid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("name=\"")+6);
                        name = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("shortname=\"")+11);
                        shortName = temp.substring(0, temp.indexOf("\""));
                        ground = new Ground(getTex(tid), name, shortName);
                        grounds.addElement(ground);
                        D.grounds.add(ground);
                        break;
                    case "Cave":
                        temp = line.substring(line.indexOf("tid=")+4);
                        tid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("name=\"")+6);
                        name = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("shortname=\"")+11);
                        shortName = temp.substring(0, temp.indexOf("\""));
                        ground = new Ground(getTex(tid), name, shortName);
                        caveGrounds.addElement(ground);
                        D.caveGrounds.add(ground);
                        break;
                    case "Object":
                        temp = line.substring(line.indexOf("tid=")+4);
                        tid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("mid=")+4);
                        mid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("matid=")+6);
                        matid = Integer.parseInt(spaceMax(temp));
                        temp = line.substring(line.indexOf("name=\"")+6);
                        name = temp.substring(0, temp.indexOf("\""));
                        temp = line.substring(line.indexOf("shortname=\"")+11);
                        shortName = temp.substring(0, temp.indexOf("\""));
                        structure = new Structure(name, shortName, getTex(tid), getModel(mid), getMaterials(matid));
                        objects.addElement(structure);
                        D.objects.add(structure);
                        break;
                }
            }
            
        }
        
        HouseCalc.groundsList.setModel(grounds);
        HouseCalc.floorsList.setModel(floors);
        HouseCalc.wallsList.setModel(walls);
        HouseCalc.roofsList.setModel(roofs);
        HouseCalc.structuresList.setModel(objects);
        HouseCalc.groundsList.setSelectedIndex(0);
        HouseCalc.floorsList.setSelectedIndex(0);
        HouseCalc.wallsList.setSelectedIndex(0);
        HouseCalc.roofsList.setSelectedIndex(0);
        HouseCalc.structuresList.setSelectedIndex(0);
        HouseCalc.groundsList.repaint();
    }
    
    private static void specials() throws FileNotFoundException, IOException {
        String slash = System.getProperty("file.separator");
        String str;
        
        str = "Data"+slash+"Specials"+slash+"skybox.png";
        D.skybox = new Tex(str);
        
        str = "Data"+slash+"Specials"+slash+"water.png";
        D.water = new Tex(str);
        
        
        
        str = "Data"+slash+"Specials"+slash+"side.obj";
        D.side = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"sideCorner.obj";
        D.sideCorner = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"sideCut.obj";
        D.sideCut = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"sideToSpine.obj";
        D.sideToSpine = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spine.obj";
        D.spine = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineCorner.obj";
        D.spineCorner = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineCornerUp.obj";
        D.spineCornerUp = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineCross.obj";
        D.spineCross = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineEnd.obj";
        D.spineEnd = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineEndUp.obj";
        D.spineEndUp = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineTCross.obj";
        D.spineTCross = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"spineTip.obj";
        D.spineTip = new ObjectData(str);
        
        str = "Data"+slash+"Specials"+slash+"levelsCross.obj";
        D.levelsCross = new ObjectData(str);
    }
    
    private static int countCommands(File file) throws FileNotFoundException, IOException {
        int commands;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            commands = 0;
            while ((line = in.readLine())!=null) {
                if (!line.trim().isEmpty()) {
                    commands++;
                }
            }
        }
        return commands;
    }
    
}
