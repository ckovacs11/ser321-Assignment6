package ser321.assign6.ckovacs1.java.server;
/*
* Copyright 2020 Curtis Kovacs,
*
* This software is the intellectual property of the author, and can not be
distributed, used, copied, or
* reproduced, in whole or in part, for any purpose, commercial or otherwise.
The author grants the ASU
* Software Engineering program the right to copy, execute, and evaluate this
work for the purpose of
* determining performance of the author in coursework, and for Software
Engineering program evaluation,
* so long as this copyright and right-to-use statement is kept in-tact in such
use.
* All other uses are prohibited and reserved to the author.
*
* Purpose: The interface for the seriesLibrary
*
* Ser321 Principles of Distributed Software Systems
* see http://pooh.poly.asu.edu/Ser321
* @author Curtis Kovacs ckovacs1@asu.edu
*
Software Engineering, CIDSE, IAFSE, ASU Poly
* @version March 2020
*/






    public interface SeriesLibrary {
    public String[] getSeriesSeasonList();
    public SeriesSeason getSeriesSeasonObj(String title);
    public boolean addSeriesSeason(SeriesSeason ss);
    public boolean removeSeriesSeason(SeriesSeason s);
    public boolean saveLibraryToFile();
    public boolean restoreLibraryFromFile();


}
