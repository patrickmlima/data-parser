import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dataparser.check.DataCheck;

import java.util.LinkedList;
import java.util.Scanner;


public class DataParser {
	private String filePath;
	private String newFilePath;
	Scanner sc = new Scanner(System.in);
	
	public DataParser() throws IOException {
		filePath = "dados_clima.txt";
		newFilePath = "dados_clima_treated.txt";
	}
	
	public void deleteInvalidLines() throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(newFilePath));
		LinkedList<String> invalidDataDates = new LinkedList<String>();
		String line = null;
		String line0 = null;
		String vLine[];
		String date = null;
		int counter = 0;
		int turn = 0;
		boolean firstLine = true;
		while((line = fileReader.readLine()) != null) {
			vLine = line.split(";");
			if(turn == 0) {
				line0 = line;
				date = vLine[1];
				++turn;
			} else {
				if(vLine[1].equals(date)) {
					turn = 0;
					if(!firstLine)
						fileWriter.write("\n");
					else
						firstLine = false;
					fileWriter.write(line0);
					fileWriter.write("\n" + line);
				} else {
					invalidDataDates.add(date);
					date = vLine[1];
					line0 = line;
					++counter;
				}
			}
			
		}
		fileReader.close();
		fileWriter.close();
		System.out.println("Número de dados inválidos: "+counter +"---"+invalidDataDates.size());
		System.out.println("------------------------------------------");
		System.out.println("Datas com dados inválidos:");
		for(String str : invalidDataDates) {
			System.out.println(str);
		}
		System.out.println("------------------------------------------\n");
	}
	
	public void checkNewFile() {
		DataCheck dtCheck = new DataCheck();
		try {
			dtCheck.checkMissingData(newFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDefinitiveFiles() throws IOException {
		LinkedList<String> tMax,tMin,relHum,windVel;
		tMax = new LinkedList<String>();
		tMin = new LinkedList<String>();
		relHum = new LinkedList<String>();
		windVel = new LinkedList<String>();
		
		LinkedList<LinkedList<String>> inputs = new LinkedList<LinkedList<String>>();
		LinkedList<String> outputs = new LinkedList<String>();
		LinkedList<String> invalidDataDates = new LinkedList<String>();
		LinkedList<Integer> aYearIndexes = new LinkedList<Integer>();
		
		BufferedReader fileReader = new BufferedReader(new FileReader(newFilePath));
		
		BufferedWriter bwIFile = new BufferedWriter(new FileWriter("dados_clima_input.txt"));
		BufferedWriter bwOFile = new BufferedWriter(new FileWriter("dados_clima_output.txt"));
		
		String line;
		String tempMax = null;
		String tempMin = null;;
		String rainFall = null;;
		String relMeanHumidity = null;
		String windMeanVelocity = null;
		String vLine[];
		int counter = 0;
		int indexes = 0;
		String aYear = "31/12/2014";
//		boolean firstLine =  true;
		int turn = 0;
		while((line = fileReader.readLine()) != null) {
			vLine = line.split(";");
			if(turn == 0) {
				tempMax = vLine[vLine.length-4];
				relMeanHumidity = vLine[vLine.length-2];
				windMeanVelocity = vLine[vLine.length-1];
				++turn;
			} else {				
				tempMin = vLine[vLine.length-1];
				rainFall = vLine[vLine.length-3];
				
				if((!tempMin.isEmpty() && !tempMax.isEmpty()) && (!relMeanHumidity.isEmpty() && !windMeanVelocity.isEmpty()) && !rainFall.isEmpty()) {
					tMin.add(tempMin);
					tMax.add(tempMax);
					relHum.add(relMeanHumidity);
					windVel.add(windMeanVelocity);
					outputs.add(rainFall);
					if(vLine[1].contains(aYear)) {
						aYearIndexes.add(indexes);
					}
					++indexes;
//					if(!firstLine) {
//						bwIFile.write("\n");
//						bwOFile.write("\n");
//					} else
//						firstLine = false;
//					bwIFile.write(tempMax + " " + tempMin + " " + relMeanHumidity + " " + windMeanVelocity + ";");
//					bwOFile.write(rainFall + ";");
				} else { 
					++counter;
					invalidDataDates.add(vLine[1]);
				}
				turn = 0;
			}
		}
		inputs.add(tMin);
		inputs.add(tMax);
		inputs.add(relHum);
		inputs.add(windVel);
		for(LinkedList<String> l : inputs) {
			for(int i = 0; i < l.size(); i++) {
				bwIFile.write(l.get(i));
				if((i+1)!=l.size())
					bwIFile.write(" ");
				else
					bwIFile.write(";\n");
			}
		}
		for(int i = 0; i < outputs.size(); i++) {
			bwOFile.write(outputs.get(i));
			if((i+1)!=outputs.size())
				bwOFile.write(" ");
			else
				bwOFile.write(";\n");
		}
		
		bwIFile.close();
		bwOFile.close();
		fileReader.close();	
		System.out.println("Quantidade de dados incompletos: " + counter);
		System.out.println("----------------------------------------");
		System.out.println("datas com dados incompletos:");
		for(String str : invalidDataDates) {
			System.out.println(str);
		}
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("Indices dos dados do ano "+aYear);
		for(Integer index : aYearIndexes) {
			System.out.println(index);
		}
		System.out.println();
	}
	
	public void printVector(String v[]) {
		System.out.println("===============================");
		for(String s : v) {
			System.out.println(s);
		}
		System.out.println("===============================\n");
	}
	
	public static void main(String[] args) throws IOException {
		DataParser parser = new DataParser();
		parser.deleteInvalidLines();
		parser.checkNewFile();
		parser.createDefinitiveFiles();
		System.out.println("Processamento do arquivo finalizado.");
	}

}
