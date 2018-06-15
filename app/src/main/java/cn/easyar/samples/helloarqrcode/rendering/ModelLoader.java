package cn.easyar.samples.helloarqrcode.rendering;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/* Class used to load .obj and get all data needed from it */
public class ModelLoader {

    private Context context;
    private int modelLinesCount;

    private ArrayList<Vector3> vertexList;
    private ArrayList<Vector3> normalList;
    /*private ArrayList<Vector3> textureList;*/
    private ArrayList<Face> facesList;
    public int numFaces;

    public short[] indices;
    public float[] verts;
    /*private float[] norms;*/

    /*private float[] vtx;*/
    public FloatBuffer vertsBuffer;/*
    private FloatBuffer textsBuffer;
    private FloatBuffer normsBuffer;*/
    public ShortBuffer indicesBuffer;

    public ModelLoader(Context context){
        this.context = context;
        vertexList = new ArrayList<>();
        normalList = new ArrayList<>();
        /*textureList = new ArrayList<>();*/
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

            /*Vertex normals*/
            if (line.startsWith("vn ")){
                String[] normals = line.split("[ ]");
                normalList.add(new Vector3(Float.parseFloat(normals[1]), Float.parseFloat(normals[2]), Float.parseFloat(normals[3])));
            }

            /*Texture coordinates*/
            if (line.startsWith("vt ")){
                String[] texture = line.split("[ ]");
                /*Nothing for now*/
                continue;
            }

            /*Poligonal face element*/
            if (line.startsWith("f ")){
                String[] faces = line.split("[ ]");

                String[] x = faces[1].split("[/]");
                String[] y = faces[2].split("[/]");
                String[] z = faces[3].split("[/]");

                /*Faces indices start from 1 , so we subtract one*/
                facesList.add(new Face(
                        (Integer.valueOf(x[0])-1),
                        (Integer.valueOf(y[0])-1),
                        (Integer.valueOf(z[0])-1)));
                        /*(Integer.valueOf(x[2])-1)));*/ /*TODO check this line code*/

                numFaces++;
            }
        }


        /*Indices setup*/
        int indx = 0;
        indices = new short[numFaces * 3];
        for(int i=0; i < numFaces; i++){
            indices[indx++] = (short) facesList.get(i).x;
            indices[indx++] = (short) facesList.get(i).y;
            indices[indx++] = (short) facesList.get(i).z;
        }

        /*Vertex */
        indx = 0;
        verts = new float[vertexList.size() * 3];
        for (int i=0; i < vertexList.size(); i++){
            verts[indx++] = vertexList.get(i).x;
            verts[indx++] = vertexList.get(i).y;
            verts[indx++] = vertexList.get(i).z;
        }


       /* indx = 0;
        vtx = new float[numFaces*9];
        for(int i = 0; i < numFaces; i++)
        {
            vtx[indx++] = vertexList.get(facesList.get(i).x).x;
            vtx[indx++] = vertexList.get(facesList.get(i).x).y;
            vtx[indx++] = vertexList.get(facesList.get(i).x).z;

            vtx[indx++] = vertexList.get(facesList.get(i).y).x;
            vtx[indx++] = vertexList.get(facesList.get(i).y).y;
            vtx[indx++] = vertexList.get(facesList.get(i).y).z;

            vtx[indx++] = vertexList.get(facesList.get(i).z).x;
            vtx[indx++] = vertexList.get(facesList.get(i).z).y;
            vtx[indx++] = vertexList.get(facesList.get(i).z).z;

        }*/

        vertsBuffer = ByteBuffer.allocateDirect(verts.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertsBuffer.put(verts).position(0);

        indicesBuffer = ByteBuffer.allocateDirect(indices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indicesBuffer.put(indices).position(0);
    }


    class Face{
        int x, y, z, n;

        public Face(int ...coords){
            x = coords[0];
            y = coords[1];
            z = coords[2];
            //n = coords[3];
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
