package test;

import java.lang.*;
import java.io.*;
import java.util.Vector;
import models.State;
import models.ValidStateGenerator;

public class SerializationTest{
    static final String filename = "SerializationTestFile.obj";
    static final int instancesCountPerRank = 3;

    public static void main(String[] args){
	write();
	read();
    }
    private static Vector<State> generateInstances(){
	Vector<State> instances = new Vector<State>();
	for(int rank = 2; rank <= 4; rank++){
	    for(int i = 0; i < instancesCountPerRank; i++){
		State instance = ValidStateGenerator.get(rank);
		instances.add(instance);
	    }
	}
	return instances;
    }
    private static void write(){
	try{
	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));

	    System.out.println("Writing data into: "+filename);
	    Vector<State> instances = generateInstances();
	    for(State instance: instances){
		System.out.println(instance);
	    }
	    oos.writeObject(instances);
	    oos.close();
	}catch(FileNotFoundException e){
	    e.printStackTrace();
	}catch(IOException e){
	    e.printStackTrace();
	}
    }
    private static void read(){
	try{
	    ObjectInputStream oos = new ObjectInputStream(new FileInputStream(new File(filename)));

	    System.out.println("Reading from: "+filename);
	    Vector<State> instances = new Vector<State>();
	    instances = (Vector<State>) oos.readObject();
	    for(State instance: instances){
		System.out.println(instance);
	    }
	    oos.close();
	}catch(ClassNotFoundException e){
	    e.printStackTrace();
	}catch(FileNotFoundException e){
	    e.printStackTrace();
	}catch(IOException e){
	    e.printStackTrace();
	}
    }
}
