package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // Belirli bir hastanın randevularını getirir
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);
    
    // Belirli bir doktorun randevularını getirir
    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeDesc(Long doctorId);
    
    // Belirli bir doktorun belirli bir tarihte randevularını getirir
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = ?1 AND DATE(a.appointmentDateTime) = DATE(?2) ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDateTime date);
    
    // Belirli bir doktorun bugünkü randevularını getirir
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = ?1 AND DATE(a.appointmentDateTime) = CURRENT_DATE ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findTodaysAppointmentsByDoctorId(Long doctorId);
    
    // Belirli bir doktorun bekleyen randevularını getirir
    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateTimeAsc(Long doctorId, Appointment.AppointmentStatus status);
    
    // Belirli bir tarih ve saatte doktorun başka randevusu var mı kontrol eder
    boolean existsByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime dateTime);
    
    // Belirli bir hastanın gelecek randevularını getirir
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = ?1 AND a.appointmentDateTime > CURRENT_TIMESTAMP ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingAppointmentsByPatientId(Long patientId);
    
    // Belirli bir hastanın geçmiş randevularını getirir
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = ?1 AND a.appointmentDateTime < CURRENT_TIMESTAMP ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findPastAppointmentsByPatientId(Long patientId);
} 