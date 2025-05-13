package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Yeni bir randevu oluşturur
     */
    public Appointment createAppointment(Long patientId, Long doctorId, LocalDateTime dateTime) {
        // Doktor ve hastanın varlığını kontrol et
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Hasta bulunamadı"));
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doktor bulunamadı"));
        
        // Doktorun rolünü kontrol et
        boolean isDoctor = doctor.getRoles().stream()
                .anyMatch(role -> "DOCTOR".equals(role.getName()));
        if (!isDoctor) {
            throw new IllegalArgumentException("Seçilen kullanıcı bir doktor değil");
        }
        
        // Hastanın rolünü kontrol et
        boolean isPatient = patient.getRoles().stream()
                .anyMatch(role -> "PATIENT".equals(role.getName()));
        if (!isPatient) {
            throw new IllegalArgumentException("Seçilen kullanıcı bir hasta değil");
        }
        
        // Tarih geçmiş mi kontrol et
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Randevu tarihi geçmiş olamaz");
        }
        
        // Çakışma kontrolü
        if (appointmentRepository.existsByDoctorIdAndAppointmentDateTime(doctorId, dateTime)) {
            throw new IllegalArgumentException("Doktorun bu tarih ve saatte başka bir randevusu bulunmaktadır");
        }
        
        // Randevu oluştur
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);
        
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Randevu durumunu günceller
     */
    public Appointment updateAppointmentStatus(Long appointmentId, Long doctorId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Randevu bulunamadı"));
        
        // Doktor yetki kontrolü
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new AccessDeniedException("Bu randevuyu sadece ilgili doktor güncelleyebilir");
        }
        
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Doktor notunu günceller
     */
    public Appointment updateDoctorNotes(Long appointmentId, Long doctorId, String notes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Randevu bulunamadı"));
        
        // Doktor yetki kontrolü
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new AccessDeniedException("Bu randevuya sadece ilgili doktor not ekleyebilir");
        }
        
        appointment.setDoctorNotes(notes);
        return appointmentRepository.save(appointment);
    }
    
    /**
     * Bir hastanın tüm randevularını getirir
     */
    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
    }
    
    /**
     * Bir hastanın gelecek randevularını getirir
     */
    public List<Appointment> getUpcomingPatientAppointments(Long patientId) {
        return appointmentRepository.findUpcomingAppointmentsByPatientId(patientId);
    }
    
    /**
     * Bir hastanın geçmiş randevularını getirir
     */
    public List<Appointment> getPastPatientAppointments(Long patientId) {
        return appointmentRepository.findPastAppointmentsByPatientId(patientId);
    }
    
    /**
     * Bir doktorun tüm randevularını getirir
     */
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeDesc(doctorId);
    }
    
    /**
     * Bir doktorun bugünkü randevularını getirir
     */
    public List<Appointment> getTodaysDoctorAppointments(Long doctorId) {
        return appointmentRepository.findTodaysAppointmentsByDoctorId(doctorId);
    }
    
    /**
     * Bir doktorun bekleyen randevularını getirir
     */
    public List<Appointment> getPendingDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdAndStatusOrderByAppointmentDateTimeAsc(
            doctorId, Appointment.AppointmentStatus.PENDING);
    }
    
    /**
     * Randevu detaylarını getirir
     */
    public Appointment getAppointmentDetails(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Randevu bulunamadı"));
    }
} 