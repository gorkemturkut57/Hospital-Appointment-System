package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patient")
public class PatientAppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Hasta randevu sayfasını gösterir
     */
    @GetMapping("/appointments")
    public String viewAppointments(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        Long patientId = currentUser.getUser().getId();
        
        // Gelecek randevuları getir
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingPatientAppointments(patientId);
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        
        // Geçmiş randevuları getir
        List<Appointment> pastAppointments = appointmentService.getPastPatientAppointments(patientId);
        model.addAttribute("pastAppointments", pastAppointments);
        
        return "patient/appointments";
    }
    
    /**
     * Hasta için yeni randevu oluşturma sayfasını gösterir
     */
    @GetMapping("/book-appointment")
    public String showBookAppointmentForm(Model model) {
        // Tüm doktorları getir
        List<User> doctors = userRepository.findAllDoctors();
        model.addAttribute("doctors", doctors);
        
        return "patient/book_appointment";
    }
    
    /**
     * Hasta için yeni randevu oluşturur
     */
    @PostMapping("/book-appointment")
    public String bookAppointment(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime appointmentTime,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Tarih ve saati birleştir
            LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
            
            // Randevu oluştur
            appointmentService.createAppointment(currentUser.getUser().getId(), doctorId, appointmentDateTime);
            
            redirectAttributes.addFlashAttribute("successMessage", "Randevunuz başarıyla oluşturuldu!");
            return "redirect:/patient/appointments";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/patient/book-appointment";
        }
    }
    
    /**
     * Randevu detaylarını gösterir
     */
    @GetMapping("/appointment/{id}")
    public String viewAppointmentDetails(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable("id") Long appointmentId,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            Appointment appointment = appointmentService.getAppointmentDetails(appointmentId);
            
            // Hasta yetki kontrolü
            if (!appointment.getPatient().getId().equals(currentUser.getUser().getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bu randevuyu görüntüleme yetkiniz yok");
                return "redirect:/patient/appointments";
            }
            
            model.addAttribute("appointment", appointment);
            return "patient/appointment_details";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/patient/appointments";
        }
    }
} 