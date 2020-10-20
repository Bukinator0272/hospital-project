package org.example.model.dao;


import org.example.model.dao.constant.TableColumns;
import org.example.model.dao.executor.Executor;
import org.example.model.entity.Doctor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorDAOImpl implements DAO<Doctor, Long> {

    private final Executor executor;

    public DoctorDAOImpl() {
        this.executor = new Executor();
    }

    @Override
    public void add(Doctor doctor) throws SQLException {
        executor.execUpdate("INSERT INTO DOCTOR (" +
                        TableColumns.DoctorTable.NAME + ',' +
                        TableColumns.DoctorTable.SURNAME + ',' +
                        TableColumns.DoctorTable.PATRONYMIC + ',' +
                        TableColumns.DoctorTable.SPECIALIZATION +
                        ") VALUES (?, ?, ?, ?)",
                doctor.getName(),
                doctor.getSurname(),
                doctor.getPatronymic(),
                doctor.getSpecialization());
    }

    @Override
    public Doctor get(Long id) throws SQLException {
        return executor.execQuery(result -> {
                    if (!result.next())
                        return null;
                    return new Doctor(
                            id,
                            result.getString(TableColumns.DoctorTable.NAME),
                            result.getString(TableColumns.DoctorTable.SURNAME),
                            result.getString(TableColumns.DoctorTable.PATRONYMIC),
                            result.getString(TableColumns.DoctorTable.SPECIALIZATION));
                }, "SELECT * FROM DOCTOR WHERE " + TableColumns.DoctorTable.ID + " = ?",
                id.toString());
    }

    @Override
    public void update(Doctor doctor) throws SQLException {
        executor.execUpdate("UPDATE DOCTOR SET " +
                        TableColumns.DoctorTable.NAME + " = ?," +
                        TableColumns.DoctorTable.SURNAME + " = ?," +
                        TableColumns.DoctorTable.PATRONYMIC + " = ?," +
                        TableColumns.DoctorTable.SPECIALIZATION + " = ? WHERE " +
                        TableColumns.DoctorTable.ID + " = ?",
                doctor.getName(),
                doctor.getSurname(),
                doctor.getPatronymic(),
                doctor.getSpecialization(),
                doctor.getId().toString());
    }

    @Override
    public void delete(Doctor doctor) throws SQLException {
        executor.execUpdate("DELETE FROM DOCTOR WHERE " + TableColumns.DoctorTable.ID + " = ?",
                doctor.getId().toString());
    }

    @Override
    public List<Doctor> getAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        return executor.execQuery(result -> {
            while (result.next()) {
                doctors.add(new Doctor(
                        result.getLong(TableColumns.DoctorTable.ID),
                        result.getString(TableColumns.DoctorTable.NAME),
                        result.getString(TableColumns.DoctorTable.SURNAME),
                        result.getString(TableColumns.DoctorTable.PATRONYMIC),
                        result.getString(TableColumns.DoctorTable.SPECIALIZATION)));
            }
            return doctors;
        }, "SELECT * FROM DOCTOR");
    }

    public Map<Long, Integer> getStatistics() throws SQLException {
        String query = "select doctor_id, count(*) from DOCTOR inner join recipe using (doctor_id) group by doctor_id";
        Map<Long, Integer> statistics = new HashMap<>();
        return executor.execQuery(result -> {
            while (result.next()) {
                statistics.put(
                        result.getLong(1),
                        result.getInt(2)
                );
            }
            return statistics;
        }, query);
    }

}
