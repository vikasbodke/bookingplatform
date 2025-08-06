-- Insert sample users (skip if already exists)
INSERT INTO app_users (name, email, phone, created_at, updated_at)
VALUES 
('Alice Johnson', 'alice@example.com', '+1234567890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Smith', 'bob@example.com', '+1987654321', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Charlie Brown', 'charlie@example.com', '+1122334455', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Insert sample theater partners (skip if already exists)
INSERT INTO theater_partners (
    company_name, 
    contact_person, 
    email, 
    phone, 
    address, 
    tax_id, 
    is_active, 
    created_at, 
    updated_at
) VALUES
('CineMax Theaters', 'John Doe', 'info@cinemax.com', '+18005551234', 
 '123 Movie Lane, Hollywood, CA', 'TAX123456789', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Silver Screen Cinemas', 'Jane Smith', 'contact@silverscreen.com', '+18005554321', 
 '456 Film Street, Los Angeles, CA', 'TAX987654321', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Starlight Theaters', 'Robert Johnson', 'support@starlight.com', '+18005556789', 
 '789 Cinema Blvd, Beverly Hills, CA', 'TAX456789123', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Insert sample cities (skip if already exists)
INSERT INTO cities (name, state, country, created_at, updated_at)
VALUES 
('Los Angeles', 'California', 'USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('New York', 'New York', 'USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chicago', 'Illinois', 'USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample theaters (skip if already exists)
INSERT INTO theaters (name, location, city_id, partner_id, number_of_screens, contact_phone, is_active, created_at, updated_at)
VALUES 
('CineMax Downtown', '123 Cinema Street, Los Angeles', 1, 1, 10, '+12135551234', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CineMax Uptown', '456 Movie Ave, Los Angeles', 1, 1, 8, '+12135551235', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Silver Screen Manhattan', '100 Broadway, New York', 2, 2, 12, '+12125551234', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Starlight Chicago', '300 Michigan Ave, Chicago', 3, 3, 15, '+17735551234', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;

-- Insert sample screens (skip if already exists)
INSERT INTO screens (theater_id, screen_number, capacity, audio_type, format, is_active, created_at, updated_at)
VALUES
-- CineMax Downtown screens
(1, 1, 150, 'Dolby Atmos', 'IMAX', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 2, 100, 'Dolby 7.1', 'Standard', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- CineMax Uptown screens
(2, 1, 120, 'Dolby 7.1', '3D', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 80, '5.1', 'Standard', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Silver Screen Manhattan screens
(3, 1, 200, 'Dolby Atmos', 'IMAX', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 150, 'Dolby 7.1', '4DX', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Starlight Chicago screens
(4, 1, 180, 'Dolby Atmos', 'IMAX', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, 100, 'Dolby 7.1', '3D', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (theater_id, screen_number) DO NOTHING;

-- Insert sample movies (skip if already exists)
INSERT INTO movies (title, genre, release_date, duration, languages, formats, created_at, updated_at)
VALUES
('The Last Adventure', 'Action', '2024-12-15', 145, '["English", "Spanish"]', '["2D", "3D"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Love in Paris', 'Romance', '2024-11-20', 120, '["English", "French"]', '["2D"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Galaxy Wars: New Dawn', 'Sci-Fi', '2024-12-25', 155, '["English"]', '["2D", "3D", "IMAX"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Haunting', 'Horror', '2024-10-31', 110, '["English", "Spanish"]', '["2D"]', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;

-- Insert sample showtimes (for the next 7 days, skip if already exists)
INSERT INTO showtimes (movie_id, language, theater_id, screen_id, format, start_time, end_time, is_active, created_at, updated_at)
VALUES
-- Day 1 - The Last Adventure (Action) - Multiple showtimes across different formats
-- CineMax Downtown
(1, 'English', 1, 1, 'IMAX', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '10 hours', 
  CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '12 hours' + INTERVAL '25 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'English', 1, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '13 hours', 
  CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '15 hours' + INTERVAL '25 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Spanish', 1, 1, 'IMAX', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '16 hours', 
  CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '18 hours' + INTERVAL '25 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Day 1 - Love in Paris (Romance) - Multiple showtimes
(2, 'English', 1, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '10 hours', 
  CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '12 hours', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'French', 2, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '13 hours', 
  CURRENT_TIMESTAMP + INTERVAL '1 day' + INTERVAL '15 hours', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Day 2 - Galaxy Wars: New Dawn (Sci-Fi) - Multiple formats
(3, 'English', 2, 1, '3D', CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '11 hours', 
  CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '13 hours' + INTERVAL '35 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'English', 3, 1, 'IMAX', CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '14 hours', 
  CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '16 hours' + INTERVAL '35 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'English', 4, 1, '3D', CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '17 hours', 
  CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '19 hours' + INTERVAL '35 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Day 3 - The Haunting (Horror) - Evening showtimes
(4, 'English', 3, 2, '4DX', CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '18 hours', 
  CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '19 hours' + INTERVAL '50 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Spanish', 4, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '20 hours', 
  CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '21 hours' + INTERVAL '50 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Day 4-7 - Mixed showtimes
-- Day 4
(1, 'English', 1, 1, 'IMAX', CURRENT_TIMESTAMP + INTERVAL '4 days' + INTERVAL '14 hours', 
  CURRENT_TIMESTAMP + INTERVAL '4 days' + INTERVAL '16 hours' + INTERVAL '25 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'English', 2, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '4 days' + INTERVAL '16 hours', 
  CURRENT_TIMESTAMP + INTERVAL '4 days' + INTERVAL '18 hours', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  
-- Day 5
(3, 'English', 3, 1, 'IMAX', CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '15 hours', 
  CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '17 hours' + INTERVAL '35 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'English', 4, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '18 hours', 
  CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '19 hours' + INTERVAL '50 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  
-- Day 6
(1, 'Spanish', 1, 1, 'IMAX', CURRENT_TIMESTAMP + INTERVAL '6 days' + INTERVAL '13 hours', 
  CURRENT_TIMESTAMP + INTERVAL '6 days' + INTERVAL '15 hours' + INTERVAL '25 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'English', 2, 1, '3D', CURRENT_TIMESTAMP + INTERVAL '6 days' + INTERVAL '16 hours', 
  CURRENT_TIMESTAMP + INTERVAL '6 days' + INTERVAL '18 hours' + INTERVAL '35 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  
-- Day 7
(2, 'French', 3, 2, '2D', CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '14 hours', 
  CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '16 hours', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'English', 4, 1, '4DX', CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '19 hours', 
  CURRENT_TIMESTAMP + INTERVAL '7 days' + INTERVAL '20 hours' + INTERVAL '50 minutes', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;

-- Insert showtime availability (skip if already exists)
INSERT INTO showtime_availability (showtime_id, available_seats, is_fully_booked, last_updated)
SELECT 
    s.showtime_id,
    (SELECT capacity FROM screens WHERE screen_id = s.screen_id) as available_seats,
    FALSE as is_fully_booked,
    CURRENT_TIMESTAMP as last_updated
FROM showtimes s
;

-- Insert first booking (if not exists)
INSERT INTO bookings (user_id, showtime_id, screen_id, booking_time, total_amount, seat_hash, status, created_at, updated_at)
SELECT 1, showtime_id, 1, CURRENT_TIMESTAMP, 25.99, 'A1A2', 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM showtimes 
WHERE showtime_id = (SELECT showtime_id FROM showtimes ORDER BY showtime_id LIMIT 1)
AND NOT EXISTS (
    SELECT 1 FROM bookings 
    WHERE user_id = 1 
    AND showtime_id = (SELECT showtime_id FROM showtimes ORDER BY showtime_id LIMIT 1) 
    AND status = 'CONFIRMED'
);

-- Insert seats for first booking (only if they don't already exist)
WITH booking_info AS (
    SELECT 
        b.booking_id,
        b.showtime_id,
        b.screen_id
    FROM bookings b
    WHERE b.user_id = 1 
    AND b.showtime_id = (SELECT showtime_id FROM showtimes ORDER BY showtime_id LIMIT 1)
    AND b.status = 'CONFIRMED'
    LIMIT 1
)
INSERT INTO seat_bookings (showtime_id, row_label, seat_number, status, booking_reference, created_at, updated_at)
SELECT 
    bi.showtime_id,
    'A' as row_label,
    s.seat_number,
    'BOOKED' as status,
    CAST('550e8400-e29b-41d4-a716-446655440000' AS UUID) as booking_reference,
    CURRENT_TIMESTAMP as created_at,
    CURRENT_TIMESTAMP as updated_at
FROM 
    booking_info bi
CROSS JOIN 
    generate_series(1, 2) AS s(seat_number)
WHERE NOT EXISTS (
    SELECT 1 
    FROM seat_bookings sb
    WHERE sb.showtime_id = bi.showtime_id 
    AND sb.row_label = 'A'
    AND sb.seat_number = s.seat_number
)
AND EXISTS (SELECT 1 FROM booking_info);

-- Insert second booking (if not exists)
INSERT INTO bookings (user_id, showtime_id, screen_id, booking_time, total_amount, seat_hash, status, created_at, updated_at)
SELECT 2, showtime_id, 1, CURRENT_TIMESTAMP, 77.94, 'B3B4B5', 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM showtimes 
WHERE showtime_id = (SELECT showtime_id FROM showtimes ORDER BY showtime_id OFFSET 1 LIMIT 1)
AND NOT EXISTS (
    SELECT 1 FROM bookings 
    WHERE user_id = 2 
    AND showtime_id = (SELECT showtime_id FROM showtimes ORDER BY showtime_id OFFSET 1 LIMIT 1) 
    AND status = 'CONFIRMED'
);

-- Insert seats for second booking (only if they don't already exist)
WITH booking_info AS (
    SELECT 
        b.booking_id,
        b.showtime_id,
        b.screen_id
    FROM bookings b
    WHERE b.user_id = 2 
    AND b.showtime_id = (SELECT showtime_id FROM showtimes ORDER BY showtime_id OFFSET 1 LIMIT 1)
    AND b.status = 'CONFIRMED'
    LIMIT 1
)
INSERT INTO seat_bookings (showtime_id, row_label, seat_number, status, booking_reference, created_at, updated_at)
SELECT 
    bi.showtime_id,
    'B' as row_label,
    s.seat_number,
    'BOOKED' as status,
    CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID) as booking_reference,
    CURRENT_TIMESTAMP as created_at,
    CURRENT_TIMESTAMP as updated_at
FROM 
    booking_info bi
CROSS JOIN 
    generate_series(3, 5) AS s(seat_number)
WHERE NOT EXISTS (
    SELECT 1 
    FROM seat_bookings sb
    WHERE sb.showtime_id = bi.showtime_id 
    AND sb.row_label = 'B'
    AND sb.seat_number = s.seat_number
)
AND EXISTS (SELECT 1 FROM booking_info);

-- Insert sample bookings with seat_booking_reference
WITH booking_inserts AS (
    INSERT INTO bookings (
        user_id, 
        showtime_id, 
        screen_id, 
        seat_booking_reference,
        booking_time, 
        total_amount, 
        seat_hash, 
        status, 
        created_at, 
        updated_at
    )
    VALUES 
    (
        1, -- user_id (Alice Johnson)
        1, -- showtime_id (The Last Adventure IMAX)
        1, -- screen_id
        '550e8400-e29b-41d4-a716-446655440001', -- seat_booking_reference
        CURRENT_TIMESTAMP - INTERVAL '1 day',
        15.99, -- total_amount
        'a1b2c3d4', -- seat_hash
        'CONFIRMED',
        CURRENT_TIMESTAMP - INTERVAL '1 day',
        CURRENT_TIMESTAMP - INTERVAL '1 day'
    ),
    (
        2, -- user_id (Bob Smith)
        3, -- showtime_id (Galaxy Wars: New Dawn 3D)
        2, -- screen_id
        '550e8400-e29b-41d4-a716-446655440002', -- seat_booking_reference
        CURRENT_TIMESTAMP - INTERVAL '6 hours',
        12.50, -- total_amount
        'e5f6g7h8', -- seat_hash
        'CONFIRMED',
        CURRENT_TIMESTAMP - INTERVAL '6 hours',
        CURRENT_TIMESTAMP - INTERVAL '6 hours'
    )
    RETURNING booking_id, seat_booking_reference, showtime_id, screen_id, created_at, updated_at
),
-- Insert seat bookings with proper references
seat_booking_inserts AS (
    INSERT INTO seat_bookings (
        showtime_id,
        row_label,
        seat_number,
        status,
        created_at,
        updated_at,
        booking_reference
    )
    SELECT 
        bi.showtime_id,
        'A' as row_label,
        1 as seat_number,
        'BOOKED' as status,
        bi.created_at,
        bi.updated_at,
        bi.seat_booking_reference
    FROM booking_inserts bi
    WHERE bi.booking_id = 1
    
    UNION ALL
    
    SELECT 
        bi.showtime_id,
        'B' as row_label,
        2 as seat_number,
        'BOOKED' as status,
        bi.created_at,
        bi.updated_at,
        bi.seat_booking_reference
    FROM booking_inserts bi
    WHERE bi.booking_id = 2
    
    RETURNING seat_booking_id, showtime_id
)
-- Update showtime availability based on bookings
UPDATE showtime_availability sa
SET 
    available_seats = s.capacity - COALESCE(
        (SELECT COUNT(*) 
         FROM seat_bookings sb 
         WHERE sb.showtime_id = sa.showtime_id 
         AND sb.status = 'BOOKED'),
        0
    ),
    is_fully_booked = COALESCE(
        (SELECT COUNT(*) >= s.capacity
         FROM seat_bookings sb 
         WHERE sb.showtime_id = sa.showtime_id 
         AND sb.status = 'BOOKED'),
        FALSE
    ),
    last_updated = CURRENT_TIMESTAMP
FROM screens s
JOIN showtimes st ON s.screen_id = st.screen_id
WHERE sa.showtime_id IN (1, 3)
AND st.showtime_id = sa.showtime_id;
