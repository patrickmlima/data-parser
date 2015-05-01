package dataparser.check;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataCheck {
	public DataCheck() {
		
	}

	public void checkMissingData(String filePath) throws IOException {
		String line;
		String vLine[];
		String date = null;
		int cont = 0;
		int turn = 0;
		BufferedReader bReader = new BufferedReader(new FileReader(filePath));
		while((line = bReader.readLine()) != null) {
			vLine = line.split(";");
			if(turn == 0) {
				date = vLine[1];
				++turn;
			} else {
				if(vLine[1].equals(date)) {
					turn = 0;
				} else {
					++cont;
					System.out.println("dado faltando -> " + date);
					date = vLine[1];
				}
			}
			
		}
		System.out.println("\nQuantidade de dados ausentes: " + cont);
		bReader.close();
	}

}
