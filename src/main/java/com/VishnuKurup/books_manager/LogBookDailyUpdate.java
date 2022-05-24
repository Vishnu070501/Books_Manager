package com.VishnuKurup.books_manager;

import com.VishnuKurup.books_manager.dbUtils.LibraryLogbook_DB_Util;

public class LogBookDailyUpdate extends Thread{
	LibraryLogbook_DB_Util logbook_DB = new LibraryLogbook_DB_Util();
	@Override
	public void run() {
		logbook_DB.updateLogbook();
		super.run();
	}
	
	
	
}
