package cn.easyar.samples.helloarqrcode.rendering;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/* Class used to load .obj and get all data needed from it */
public class ModelLoader {

    private Context context;
    private int modelLinesCount;

    private ArrayList<Vector3> vertexList;
    private ArrayList<Vector3> normalList;
    private ArrayList<Vector3> textureList;
    private ArrayList<Face> facesList;
    private int numFaces;

    private short[] indices;
    private float[] verts;
    private float[] norms;

    private float[] vtx;
    private FloatBuffer vertsBuffer;
    private FloatBuffer textsBuffer;
    private FloatBuffer normsBuffer;
    private FloatBuffer indicesBuffer;

    public ModelLoader(Context context){
        this.context = context;
        vertexList = new ArrayList<>();
        normalList = new ArrayList<>();
        textureList = new ArrayList<>();
        facesList = new ArrayList<>();
    }

    /* Warning - this method could consume a considerable amount of time
    * if the model is complex this method should run in a different thread and get notified when its done */
    public void load(String modelResourceName) throws IOException {
        InputStream is = context.getAssets().open(modelResourceName+".obj");
        StringBuilder model = new StringBuilder("");
        String line;

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        while (line != null){
            model.append(line);
            model.append("\n");
            line = reader.readLine();
        }
        is.close();

        parseModel(model.toString());
    }

    public void parseModel(String stringModel){
        /*TODO*/
        String[] lines = stringModel.split("\n");
        modelLinesCount = lines.length;

        for (int i=0; i < modelLinesCount; i++){
            String line = lines[i];

            /*Avoid comments*/
            if (line.startsWith("#")){
                continue;
            }

            /*Vertex*/
            if (line.startsWith("v ")){
                String[] verts = line.split("[ ]");
                vertexList.add(new Vector3(Float.parseFloat(verts[1]), Float.parseFloat(verts[2]), Float.parseFloat(verts[3])));
            }
        }
    }


    /*Populate part of the vertex list*/
    public void parseVertexList(String[] verts){
        /*TODO*/
    }

    class Face{
        int x, y, z, n;

        public Face(int ...coords){
            x = coords[0];
            y = coords[1];
            z = coords[2];
            n = coords[3];
        }
    }

    class Vector3{
        float x, y, z;

        public Vector3(){
            x = y = z = 0;
        }

        public Vector3(float ...coords){
            x = coords[0];
            y = coords[1];
            z = coords[2];
        }

        @Override
        public String toString() {
            return "( "+x+" - "+y+" - "+z+" )";
        }
    }
}
