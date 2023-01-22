package com.example.application.repos;

import com.example.application.entities.BroadcastPeriod;
import com.example.application.services.Shared;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.sql.*;


/**
 * Class containing static methods utilized to interact directly with the database.
 *
 * The reason for it is: some custom implementations are hard to be done in JPA+Spring style.
 */
public class DirectRepo {

    /**
     *
     * @param nrOfSeats
     * @return
     */
    public static BroadcastPeriod firstBroadcastPeriodByNrOfSeats(Integer nrOfSeats) {
        try {
            PreparedStatement st = Shared.getDb().prepareStatement("SELECT * FROM broadcast_periods WHERE nr_of_seats = ? LIMIT 1");
            st.setInt(1, nrOfSeats);
            ResultSet rs = st.executeQuery();

            if(!rs.isBeforeFirst()) {
                return null;
            } else {
                rs.next();

                return new BroadcastPeriod(
                    rs.getInt(rs.findColumn("id")),
                    rs.getDate(rs.findColumn("start_time")),
                    rs.getDate(rs.findColumn("end_time")),
                    null,
                    rs.getInt(rs.findColumn("nr_of_seats")),
                    rs.getDouble(rs.findColumn("price_per_seat"))
                );
            }

        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static BroadcastPeriod firstBroadcastPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            PreparedStatement st = Shared.getDb().prepareStatement(
                "SELECT * " +
                    "FROM broadcast_periods " +
                    "WHERE (? >= start_time AND ? < end_time) OR" +
                        "(? > start_time AND ? <= end_time)" +
                    "LIMIT 1;"
            );

            Timestamp start =  new Timestamp(startTime.toInstant(ZoneOffset.of("+02:00")).toEpochMilli());
            Timestamp end =  new Timestamp(endTime.toInstant(ZoneOffset.of("+02:00")).toEpochMilli());

            st.setTimestamp(1, start);
            st.setTimestamp(2, start);
            st.setTimestamp(3, end);
            st.setTimestamp(4, end);
            ResultSet rs = st.executeQuery();

            if(!rs.isBeforeFirst()) {
                return null;
            } else {
                rs.next();

                return new BroadcastPeriod(
                    rs.getInt(rs.findColumn("id")),
                    rs.getDate(rs.findColumn("start_time")),
                    rs.getDate(rs.findColumn("end_time")),
                    null,
                    rs.getInt(rs.findColumn("nr_of_seats")),
                    rs.getDouble(rs.findColumn("price_per_seat"))
                );
            }

        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}
