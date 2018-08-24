package fr.ensma.lias.bimedia2018machinelearning.generation.utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lotfi IDDIR 
 */
public class GenerDate {
	
	public Date generDate(Date begin, Date end)
	
	{
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GenerNum gener = new GenerNum();
		String beginString = sm.format(begin);
		 
		String endString = sm.format(end) ;
		
		long beginTime = Timestamp.valueOf(beginString).getTime();
		 
		long endTime = Timestamp.valueOf(endString).getTime();
		
		return new Date(gener.generLong(beginTime, endTime));
	}
	
	public Timestamp generTimestamp(String begin, String end)
	{
		GenerNum gener = new GenerNum();
		long beginTime = Timestamp.valueOf(begin).getTime();
		 
		long endTime = Timestamp.valueOf(end).getTime();
		return new Timestamp(gener.generLong(beginTime, endTime));
		
	}

}
