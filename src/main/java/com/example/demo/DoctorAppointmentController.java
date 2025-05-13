package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/doctor")
public class DoctorAppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    /**
     * Doktorun randevularını listeler
     */
    @GetMapping("/appointments")
    public String viewAppointments(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        Long doctorId = currentUser.getUser().getId();
        
        // Bekleyen randevuları getir
        List<Appointment> pendingAppointments = appointmentService.getPendingDoctorAppointments(doctorId);
        model.addAttribute("pendingAppointments", pendingAppointments);
        
        // Tüm randevuları getir
        List<Appointment> allAppointments = appointmentService.getDoctorAppointments(doctorId);
        model.addAttribute("allAppointments", allAppointments);
        
        return "doctor/appointments";
    }
    
    /**
     * Doktorun bugünkü randevularını gösterir
     */
    @GetMapping("/today-appointments")
    public String viewTodayAppointments(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        Long doctorId = currentUser.getUser().getId();
        
        // Bugünkü randevuları getir
        List<Appointment> todayAppointments = appointmentService.getTodaysDoctorAppointments(doctorId);
        model.addAttribute("todayAppointments", todayAppointments);
        
        return "doctor/today_appointments";
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
            
            // Doktor yetki kontrolü
            if (!appointment.getDoctor().getId().equals(currentUser.getUser().getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bu randevuyu görüntüleme yetkiniz yok");
                return "redirect:/doctor/appointments";
            }
            
            model.addAttribute("appointment", appointment);
            return "doctor/appointment_details";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/doctor/appointments";
        }
    }
    
    /**
     * Randevu durumunu günceller (Onay/Red)
     */
    @PostMapping("/appointment/{id}/status")
    public String updateAppointmentStatus(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable("id") Long appointmentId,
            @RequestParam Appointment.AppointmentStatus status,
            RedirectAttributes redirectAttributes) {
        
        try {
            appointmentService.updateAppointmentStatus(appointmentId, currentUser.getUser().getId(), status);
            
            String statusMessage = status == Appointment.AppointmentStatus.APPROVED 
                    ? "Randevu onaylandı" 
                    : "Randevu reddedildi";
            redirectAttributes.addFlashAttribute("successMessage", statusMessage);
            
            return "redirect:/doctor/appointment/" + appointmentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/doctor/appointment/" + appointmentId;
        }
    }
    
    /**
     * Doktor notlarını günceller
     */
    @PostMapping("/appointment/{id}/notes")
    public String updateAppointmentNotes(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable("id") Long appointmentId,
            @RequestParam String doctorNotes,
            RedirectAttributes redirectAttributes) {
        
        try {
            appointmentService.updateDoctorNotes(appointmentId, currentUser.getUser().getId(), doctorNotes);
            redirectAttributes.addFlashAttribute("successMessage", "Notlar başarıyla güncellendi");
            return "redirect:/doctor/appointment/" + appointmentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/doctor/appointment/" + appointmentId;
        }
    }
} 