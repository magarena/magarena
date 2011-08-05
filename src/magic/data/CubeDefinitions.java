package magic.data;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import magic.MagicMain;
import magic.model.MagicCubeDefinition;

public class CubeDefinitions {
	
	private static final String[] INCLUDED_CUBES={"all"};
	private static final String CUBE_FILE_EXTENSION="_cube.txt";
    
    public static final String DEFAULT_NAME=INCLUDED_CUBES[0];
	
	private static final FileFilter CUBE_FILE_FILTER=new FileFilter() {
		@Override
		public boolean accept(final File file) {
			return file.isFile()&&file.getName().endsWith(CUBE_FILE_EXTENSION);
		}
	};

	private static final CubeDefinitions INSTANCE=new CubeDefinitions();
	
	private final List<MagicCubeDefinition> cubeDefinitions;
	
	private CubeDefinitions() {
		cubeDefinitions=new ArrayList<MagicCubeDefinition>();
		for (final String cubeName : INCLUDED_CUBES) {
			cubeDefinitions.add(new MagicCubeDefinition(cubeName));
		}
	}
			
	public String[] getCubeNames() {
		final String names[]=new String[cubeDefinitions.size()];
		for (int index=0;index<names.length;index++) {
			names[index]=cubeDefinitions.get(index).getName();
		}
		return names;
	}
	
	public MagicCubeDefinition getCubeDefinition(final String name) {
		for (final MagicCubeDefinition cubeDefinition : cubeDefinitions) {
			if (cubeDefinition.getName().equals(name)) {
				return cubeDefinition;
			}
		}
		return cubeDefinitions.get(0);
	}
	
	private void loadCubeDefinition(final String name,final File file) {
        String content = "";
        try {
            content = FileIO.toStr(file);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + name);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        final Scanner sc = new Scanner(content);
		final MagicCubeDefinition cubeDefinition = new MagicCubeDefinition(name);
        while (sc.hasNextLine()) {
            final String cardName = sc.nextLine().trim();
            if (!cardName.isEmpty()) {
                cubeDefinition.add(cardName);
            }
        }
		cubeDefinitions.add(cubeDefinition);
	}
	
	public void loadCubeDefinitions() {
		final File cubeFiles[]=new File(MagicMain.getModsPath()).listFiles(CUBE_FILE_FILTER);
		if (cubeFiles!=null) {
			for (final File file : cubeFiles) {
				final String name = file.getName();
				final int index = name.indexOf(CUBE_FILE_EXTENSION);
                loadCubeDefinition(name.substring(0,index),file);
			}
		}
		
		System.err.println(cubeDefinitions.size()+" cube definitions");
		for (final MagicCubeDefinition cubeDefinition : cubeDefinitions) {
			System.err.println("Cube "+cubeDefinition.getName()+" : "+cubeDefinition.size()+" cards");
		}
	}
	
	public static CubeDefinitions getInstance() {
		
		return INSTANCE;
	}
}
