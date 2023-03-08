package pl.com.bottega.cymes.reservations;

interface CustomerInformationProvider {
    CustomerInformation getByUserId(Long userId);
}

record CustomerInformation(Long userId, String firstName, String lastName, String phoneNumber, String email) {

}
