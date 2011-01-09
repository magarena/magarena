package magic.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import magic.MagicMain;
import magic.model.MagicCubeDefinition;

public class CubeDefinitions {

	private static final CubeDefinitions INSTANCE=new CubeDefinitions();
	
	public static final String DEFAULT_NAME="default";

	private static final String DEFAULT_FILENAME="cube.txt";
	private static final String CUBE_FILE_EXTENSION="_cube.txt";

	private static final FileFilter CUBE_FILE_FILTER=new FileFilter() {

		@Override
		public boolean accept(final File file) {

			return file.isFile()&&file.getName().endsWith(CUBE_FILE_EXTENSION);
		}
	};

	private final List<MagicCubeDefinition> cubeDefinitions;
	
	private CubeDefinitions() {
		
		cubeDefinitions=new ArrayList<MagicCubeDefinition>();
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
	
	private void loadCubeDefinition(final String name,final InputStream inputStream) throws IOException {
		
		final MagicCubeDefinition cubeDefinition=new MagicCubeDefinition(name);
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new InputStreamReader(inputStream));
			while (true) {
				final String line=reader.readLine();
				if (line==null) {
					break;
				}
				final String cardName=line.trim();
				if (!cardName.isEmpty()) {
					cubeDefinition.add(cardName);
				}
			}
		} finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (final IOException ex) {}
			}
		}
		cubeDefinitions.add(cubeDefinition);
	}
	
	public void loadCubeDefinitions() throws IOException {
		
		final InputStream inputStream=this.getClass().getResourceAsStream(DEFAULT_FILENAME);
		loadCubeDefinition(DEFAULT_NAME,inputStream);
		inputStream.close();
		
		final File cubeFiles[]=new File(MagicMain.getModsPath()).listFiles(CUBE_FILE_FILTER);
		if (cubeFiles!=null) {
			for (final File file : cubeFiles) {
				
				final String name=file.getName();
				final int index=name.indexOf(CUBE_FILE_EXTENSION);
				final InputStream fileInputStream=new FileInputStream(file);
				loadCubeDefinition(name.substring(0,index),fileInputStream);
				fileInputStream.close();
			}
		}
		
		System.out.println(cubeDefinitions.size()+" cube definitions");
	}
	
	public static CubeDefinitions getInstance() {
		
		return INSTANCE;
	}
}