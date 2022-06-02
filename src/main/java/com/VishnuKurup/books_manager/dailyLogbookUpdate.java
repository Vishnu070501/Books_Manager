
// daily update run on a seperate thread
package com.VishnuKurup.books_manager;

import com.VishnuKurup.books_manager.dbUtils.LibraryLogbook_DB_Util;

public class dailyLogbookUpdate implements Runnable {

    @Override
    public void run() {
    	
    	LibraryLogbook_DB_Util logbook_DB = new LibraryLogbook_DB_Util();

    	logbook_DB.updateLogbook();
        // Do your daily job here.
    }

}
