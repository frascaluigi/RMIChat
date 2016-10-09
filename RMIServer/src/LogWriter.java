import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class LogWriter {


	private File file;
	
	private void writeLog(String string) {
		
		String formattedString = string +"\t at "+ timeNow() + "\n" ;
		
		FileWriter writer;
		try {
			writer = new FileWriter(file, true);
			writer.append(formattedString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String timeNow(){
		
		Calendar calendar = new GregorianCalendar();
		
		int ore = calendar.get(Calendar.HOUR);
		int minuti = calendar.get(Calendar.MINUTE);
		int secondi = calendar.get(Calendar.SECOND);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		String when;
		
		if(calendar.get(Calendar.AM_PM) == 0){
				when = "A.M.";
		}else{
			  	when = "P.M.";
		}
		
		String time = "[  "+day+"/"+month+"/"+year+" - "+ore+":"+minuti+":"+secondi+"  "+when+" ]";
		
		return time;
		
	}

	public LogWriter() {
		file = new File("log/log.txt");
	}		
	
	public void log(String s ){
		this.writeLog(s);
	}
	
}
