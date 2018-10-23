package application;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileMaker {
	  String[][] Mat;
	 String GraphNam,Dir;
	 StringBuilder CodeDot,CodeJSON;
	 Boolean IsOriented;
	private String FileNam;
	public FileMaker(String[][] mat, String graphNam, String dir, Boolean IsOriented) {
		this.Mat = mat;
		GraphNam = graphNam;
		Dir = dir;
		this.IsOriented=  IsOriented;
		CodeDot=new StringBuilder();
		int nmb=new File(".").listFiles().length;
		this.FileNam="Graphe_"+(nmb-6);
		}
	public Boolean makeFile() {
		try  {
		TransformToDot();
		TransformToJSON();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FileNam+".dot")));
			writer.print(CodeDot.toString());
			writer.flush();
			
			writer.close();
			
			 writer = new PrintWriter(new BufferedWriter(new FileWriter(FileNam+"JSON.TXT")));
			writer.print(CodeJSON.toString());
			writer.flush();
			
			writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		File f=new File(FileNam+".dot");
		getPng(FileNam+".png",getFile(FileNam+".dot"));
				return true;
	
		
	}
	public Boolean makeFileJSON() {
		try  {
		TransformToJSON();
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FileNam+"JSON.txt")));
			writer.print(CodeJSON.toString());
			writer.flush();
			writer.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return true;
	
		
	}
	private void TransformToJSON() {
		CodeJSON=new StringBuilder();
		CodeJSON.append("Graphe = {\n   edges : [\n");
		String  N1="Node_1:",N2="Node_2:";
		if(IsOriented) {
			N1="Start:";
			N2="END:";
		}
		int nbr=0;
		for (int i = 0; i < Mat.length; i++)
			for (int p = 0; p < Mat.length; p++) {
				
				if(Mat[p][i]=="1") {
					CodeJSON.append("    Edge_"+nbr+++" : {");
					CodeJSON.append(N1);
					CodeJSON.append(i-1);
					CodeJSON.append(",\n");
					CodeJSON.append("               "+N2);
					CodeJSON.append(p-1);
					CodeJSON.append("}\n");
				}
					
			}
		CodeJSON.append("\n]\n}");
	}
	private void TransformToDot() {
		String arc="--";
		if(IsOriented) {
			CodeDot.append("di");
			arc="->";
		}
			
		CodeDot.append("graph ");
		CodeDot.append(GraphNam+"{ ");
		for (int i = 0; i < Mat.length; i++) {
		
			int count=0;
			for (int p = 0; p < Mat.length; p++) {
				if(Mat[p][i]=="1")
				count++;
			}
				switch (count) {
				case 0:
					CodeDot.append(Mat[i][0]+";");
					break;
                  case 1:	for (int j = 0; j < Mat.length; j++) {
						if(Mat[j][i]=="1") {
							CodeDot.append(Mat[i][0]+arc+Mat[0][j]+";");break;
						}
					}
					break;

				default:
					CodeDot.append(Mat[i][0]+arc+"{");
					for (int j = 0; j < Mat.length; j++) {
						if(Mat[j][i]=="1") {
							CodeDot.append(Mat[0][j]+";");
						}
					}
					CodeDot.deleteCharAt(CodeDot.length()-1);
					CodeDot.append("};");
					break;
				}
		
		}
		
		CodeDot.append("}");
		System.out.println(CodeDot.toString());
	}
	private int CountOne(String[] tab) {
		int count=0;
		for (int i = 0; i < Mat.length; i++) {
			if(tab[i]=="1")
			count++;
		}
		return  count;
	}
    public void getPng(String ImageName,File f){
        try {
       Runtime runtime = Runtime.getRuntime();
       
     Process p=runtime.exec("dot -Tpng "+f.getName()+" -o "+ImageName);
        p.waitFor();
        
        File file = new File(ImageName );
        Desktop.getDesktop().open( file );
     
        }
        catch(IOException e1) {
            System.out.println(e1.getLocalizedMessage());
            
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	private File getFile(String fileNam) {
		File[] fileList=(new File(".")).listFiles();
		
	for (File file : fileList) {
		if(file.getName().equals(fileNam)) {
			return file;
		}
			
	}
	return null;	
	}

}
