document.addEventListener('DOMContentLoaded', () => {
    // Select all seat buttons
    const seats = document.querySelectorAll('.seat');
    const selectedSeatDisplay = document.getElementById('selectedSeatDisplay');
    const confirmBtn = document.getElementById('confirmBtn');
    const reservationForm = document.getElementById('reservationForm');
    const seatNumberInput = document.getElementById('seatNumberInput');
    const usernameInput = document.getElementById('usernameInput');

    // Track selected seats
    let selectedSeats = [];
    
    // Set the username from the session (this should be set dynamically in a real application)
    const username = 'USERNAME_FROM_SESSION'; // Replace with the actual username from session data
    
    // Add click event listener to each seat
    seats.forEach(seat => {
        seat.addEventListener('click', () => {
            // Check if seat is disabled
            if (seat.classList.contains('disabled')) {
                return;
            }
            
            // If seat is already selected, deselect it
            if (seat.classList.contains('selected')) {
                seat.classList.remove('selected');
                selectedSeats = selectedSeats.filter(selectedSeat => selectedSeat !== seat.dataset.seatNumber);
            } 
            // If seat is not selected
            else {
                // First, remove selection from any previously selected seats
                selectedSeats.forEach(seatNum => {
                    const prevSelectedSeat = document.querySelector(`.seat[data-seat-number="${seatNum}"]`);
                    if (prevSelectedSeat) {
                        prevSelectedSeat.classList.remove('selected');
                    }
                });
                
                // Clear previous selections
                selectedSeats = [];
                
                // Select the new seat
                seat.classList.add('selected');
                selectedSeats.push(seat.dataset.seatNumber);
            }
            
            // Update selected seat display
            if (selectedSeats.length > 0) {
                selectedSeatDisplay.textContent = `Selected Seat: ${selectedSeats[0]}`;
                confirmBtn.disabled = false;
                
                // Set the seat number and username in the hidden form inputs
                seatNumberInput.value = selectedSeats[0];
                usernameInput.value = username;
            } else {
                selectedSeatDisplay.textContent = '';
                confirmBtn.disabled = true;
            }
        });
    });
    
    // Confirm button click handler
    confirmBtn.addEventListener('click', () => {
        // Submit the form when the confirm button is clicked
        reservationForm.submit();
    });

    // Optionally disable some seats (e.g., already reserved seats)
    const disabledSeats = ['A3', 'B7', 'D2', 'C6']; // Example of disabled seats
    disabledSeats.forEach(seatNumber => {
        const seat = document.querySelector(`.seat[data-seat-number="${seatNumber}"]`);
        if (seat) {
            seat.classList.add('disabled');
            seat.disabled = true;
        }
    });
});
