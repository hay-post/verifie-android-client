/**
 * Java parser for the MRZ records, as specified by the ICAO organization.
 * Copyright (C) 2011 Innovatrics s.r.o.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.verifie.android.ui.mrz.parsing;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds a MRZ date type.
 *
 * @author Martin Vysny
 */
public class MrzDate implements Serializable, Comparable<MrzDate> {
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Year, 00-99.
     * <p/>
     * Note: I am unable to find a specification of conversion of this value to a full year value.
     */
    public final int year;
    /**
     * Month, 1-12.
     */
    public final int month;
    /**
     * Day, 1-31.
     */
    public final int day;

    private final String mrz;

    /**
     * Is the date valid or not
     */
    private final boolean isValidDate;

    public MrzDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        isValidDate = check();
        this.mrz = null;
    }

    public MrzDate(int year, int month, int day, String raw) {
        this.year = year;
        this.month = month;
        this.day = day;
        isValidDate = check();
        this.mrz = raw;
    }

    @Override
    public String toString() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
            Date date = dateFormat.parse(this.mrz);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String toMrz() {
        if (mrz != null) {
            return mrz;
        } else {
            return String.format("%02d%02d%02d", year, month, day);
        }
    }

    private boolean check() {
        if (year < 0 || year > 99) {
            System.out.println("Parameter year: invalid value " + year + ": must be 0..99");
            return false;
        }
        if (month < 1 || month > 12) {
            System.out.println("Parameter month: invalid value " + month + ": must be 1..12");
            return false;
        }
        if (day < 1 || day > 31) {
            System.out.println("Parameter day: invalid value " + day + ": must be 1..31");
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MrzDate other = (MrzDate) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.year;
        hash = 11 * hash + this.month;
        hash = 11 * hash + this.day;
        return hash;
    }

    public int compareTo(MrzDate o) {
        return Integer.valueOf(year * 10000 + month * 100 + day).compareTo(o.year * 10000 + o.month * 100 + o.day);
    }

    /**
     * Returns the date validity
     *
     * @return Returns a boolean true if the parsed date is valid, false otherwise
     */
    public boolean isDateValid() {
        return isValidDate;
    }
}
