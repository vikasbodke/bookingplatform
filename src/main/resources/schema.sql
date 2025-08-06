-- Users Table
CREATE TABLE IF NOT EXISTS app_users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index for email for faster lookups
CREATE INDEX IF NOT EXISTS idx_user_email ON app_users (email);

-- Theater Partners Table
CREATE TABLE IF NOT EXISTS theater_partners (
    partner_id SERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    tax_id VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for theater_partners
CREATE INDEX IF NOT EXISTS idx_partner_email ON theater_partners (email);
CREATE INDEX IF NOT EXISTS idx_partner_tax_id ON theater_partners (tax_id);

-- Cities Table
CREATE TABLE IF NOT EXISTS cities (
    city_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Theaters Table
CREATE TABLE IF NOT EXISTS theaters (
    theater_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location TEXT NOT NULL,
    city_id INTEGER NOT NULL,
    partner_id INTEGER,
    number_of_screens INTEGER NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_theater_city FOREIGN KEY (city_id) REFERENCES cities(city_id),
    CONSTRAINT fk_theater_partner FOREIGN KEY (partner_id) REFERENCES theater_partners(partner_id)
);

-- Indexes for theaters
CREATE INDEX IF NOT EXISTS idx_theater_city ON theaters (city_id);
CREATE INDEX IF NOT EXISTS idx_theater_partner ON theaters (partner_id);

-- Screens Table
CREATE TABLE IF NOT EXISTS screens (
    screen_id SERIAL PRIMARY KEY,
    theater_id INTEGER NOT NULL,
    screen_number INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    audio_type VARCHAR(50) NOT NULL,
    format VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_screen_theater FOREIGN KEY (theater_id) REFERENCES theaters(theater_id) ON DELETE CASCADE,
    CONSTRAINT uq_theater_screen UNIQUE (theater_id, screen_number)
);

-- Index for screens
CREATE INDEX IF NOT EXISTS idx_screen_theater ON screens (theater_id);

-- Movies Table
CREATE TABLE IF NOT EXISTS movies (
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    languages JSONB NOT NULL,
    formats JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for movies
CREATE INDEX IF NOT EXISTS idx_movie_title ON movies (title);
CREATE INDEX IF NOT EXISTS idx_movie_release_date ON movies (release_date);

-- Showtimes Table
CREATE TABLE IF NOT EXISTS showtimes (
    showtime_id SERIAL PRIMARY KEY,
    movie_id INTEGER NOT NULL,
    language VARCHAR(50) NOT NULL,
    theater_id INTEGER NOT NULL,
    screen_id INTEGER NOT NULL,
    format VARCHAR(10) NOT NULL DEFAULT '2D',
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_showtime_movie FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
    CONSTRAINT fk_showtime_theater FOREIGN KEY (theater_id) REFERENCES theaters(theater_id) ON DELETE CASCADE,
    CONSTRAINT fk_showtime_screen FOREIGN KEY (screen_id) REFERENCES screens(screen_id) ON DELETE CASCADE,
    CONSTRAINT chk_showtime_duration CHECK (end_time > start_time)
);

-- Indexes for showtimes
CREATE INDEX IF NOT EXISTS idx_showtime_movie ON showtimes (movie_id);
CREATE INDEX IF NOT EXISTS idx_showtime_theater ON showtimes (theater_id);
CREATE INDEX IF NOT EXISTS idx_showtime_screen ON showtimes (screen_id);
CREATE INDEX IF NOT EXISTS idx_showtime_datetime ON showtimes (start_time, end_time);

-- Showtime Availability Table
CREATE TABLE IF NOT EXISTS showtime_availability (
    availability_id SERIAL PRIMARY KEY,
    showtime_id INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    is_fully_booked BOOLEAN NOT NULL DEFAULT FALSE,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_availability_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id) ON DELETE CASCADE
);

-- Indexes for showtime_availability
CREATE INDEX IF NOT EXISTS idx_availability_showtime ON showtime_availability (showtime_id);
CREATE INDEX IF NOT EXISTS idx_availability_status ON showtime_availability (is_fully_booked);

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    showtime_id INTEGER NOT NULL,
    screen_id INTEGER,
    seat_booking_reference UUID,
    booking_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    seat_hash VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_screen FOREIGN KEY (screen_id) REFERENCES screens(screen_id) ON DELETE CASCADE
);

-- Indexes for bookings
CREATE INDEX IF NOT EXISTS idx_booking_user ON bookings (user_id);
CREATE INDEX IF NOT EXISTS idx_booking_showtime ON bookings (showtime_id);
CREATE INDEX IF NOT EXISTS idx_booking_status ON bookings (status);
CREATE INDEX IF NOT EXISTS idx_booking_seat_reference ON bookings (seat_booking_reference);

-- Seat Bookings Table
CREATE TABLE IF NOT EXISTS seat_bookings (
    seat_booking_id SERIAL PRIMARY KEY,
    showtime_id INTEGER NOT NULL,
    row_label VARCHAR(5) NOT NULL,
    seat_number INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    booking_reference UUID NOT NULL,
    CONSTRAINT fk_seatbooking_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id) ON DELETE CASCADE,
    CONSTRAINT uq_seat_showtime UNIQUE (showtime_id, row_label, seat_number)
);

-- Indexes for seat_bookings
CREATE INDEX IF NOT EXISTS idx_seatbooking_showtime ON seat_bookings (showtime_id);
CREATE INDEX IF NOT EXISTS idx_seatbooking_seat ON seat_bookings (showtime_id, row_label, seat_number);
