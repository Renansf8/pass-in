package renanferreira.com.passin.dto.event;

//record -> usado para transferência de dados
public record EventDTO(
  String id, 
  String title, 
  String slug, 
  Integer maximumAttendees, 
  Integer attendeesAmount) {
  
}
