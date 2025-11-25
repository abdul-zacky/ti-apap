-- Insert Properties (Luxury Hotels in London and Paris)
INSERT INTO property (property_id, property_name, address, province, type, description, owner_id, owner_name, active_status, total_room, income, created_date, updated_date) VALUES
('PROP-001', 'The Ritz London', '150 Piccadilly, St. James''s, London W1J 9BR, United Kingdom', '31', 1, 'Iconic 5-star luxury hotel in the heart of London, offering elegant rooms and world-class service since 1906.', '550e8400-e29b-41d4-a716-446655440001', 'Sir Charles Forte', 1, 136, 125000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-002', 'The Savoy', 'Strand, London WC2R 0EZ, United Kingdom', '31', 1, 'Historic luxury hotel on the banks of the Thames, featuring Art Deco design and Michelin-starred dining.', '550e8400-e29b-41d4-a716-446655440002', 'Fairmont Hotels', 1, 6, 98000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003', 'Claridge''s', 'Brook Street, Mayfair, London W1K 4HR, United Kingdom', '31', 1, 'Legendary 5-star hotel in Mayfair, renowned for its Art Deco heritage and impeccable British service.', '550e8400-e29b-41d4-a716-446655440003', 'Maybourne Hotel Group', 1, 50, 87500000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-004', 'Shangri-La Hotel at The Shard', '31 St Thomas Street, London SE1 9QU, United Kingdom', '31', 1, 'Ultra-luxury hotel occupying floors 34-52 of The Shard, offering breathtaking panoramic views of London.', '550e8400-e29b-41d4-a716-446655440004', 'Shangri-La Hotels', 1, 9, 112000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-005', 'Hôtel Ritz Paris', '15 Place Vendôme, 75001 Paris, France', '75', 1, 'Legendary Parisian palace hotel, epitome of French luxury and elegance since 1898.', '550e8400-e29b-41d4-a716-446655440005', 'Mohamed Al-Fayed', 1, 9, 145000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-006', 'Le Meurice', '228 Rue de Rivoli, 75001 Paris, France', '75', 1, 'Palace hotel facing the Tuileries Garden, combining 18th-century opulence with contemporary luxury.', '550e8400-e29b-41d4-a716-446655440006', 'Dorchester Collection', 1, 30, 92000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-007', 'Hôtel Plaza Athénée', '25 Avenue Montaigne, 75008 Paris, France', '75', 1, 'Iconic palace hotel on Avenue Montaigne, embodying Parisian haute couture and culinary excellence.', '550e8400-e29b-41d4-a716-446655440007', 'Dorchester Collection', 1, 30, 135000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-008', 'Four Seasons Hotel George V', '31 Avenue George V, 75008 Paris, France', '75', 1, 'Legendary palace hotel steps from the Champs-Élysées, featuring three Michelin-starred restaurants.', '550e8400-e29b-41d4-a716-446655440008', 'Four Seasons Hotels', 1, 9, 156000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-009', 'The Connaught', 'Carlos Place, Mayfair, London W1K 2AL, United Kingdom', '31', 1, 'Quintessentially British luxury hotel in Mayfair, blending Edwardian elegance with contemporary style.', '550e8400-e29b-41d4-a716-446655440009', 'Maybourne Hotel Group', 1, 0, 78000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-010', 'Mandarin Oriental Hyde Park', '66 Knightsbridge, London SW1X 7LA, United Kingdom', '31', 1, 'Elegant Victorian hotel overlooking Hyde Park, offering award-winning spa and fine dining.', '550e8400-e29b-41d4-a716-446655440010', 'Mandarin Oriental Group', 1, 0, 95000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (property_id) DO NOTHING;

-- Insert Room Types for The Ritz London
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-001-01', 'Deluxe Room', 'Elegant room with classic Louis XVI décor, marble bathroom, and luxurious amenities.', 2, 8500000, 2, 'King Bed, Marble Bathroom, WiFi, Smart TV, Mini Bar, Safe, Air Conditioning', 'PROP-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-001-02', 'Executive Suite', 'Spacious suite with separate living area, stunning views, and premium furnishings.', 3, 15000000, 4, 'King Bed, Separate Living Room, Marble Bathroom, Butler Service, WiFi, Smart TV', 'PROP-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-001-03', 'Royal Suite', 'Opulent suite with magnificent décor, private terrace, and personalized butler service.', 4, 35000000, 6, 'King Bed, Living Room, Dining Area, Private Terrace, Butler Service, Jacuzzi', 'PROP-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for The Savoy
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-002-01', 'Superior Queen Room', 'Art Deco styled room with Thames or courtyard views, featuring bespoke furnishings.', 2, 7500000, 3, 'Queen Bed, Art Deco Design, WiFi, Smart TV, Mini Bar, Rainfall Shower', 'PROP-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-002-02', 'Junior Suite', 'Spacious suite with separate seating area and views of the Thames or London skyline.', 3, 12500000, 5, 'King Bed, Seating Area, Marble Bathroom, WiFi, Nespresso Machine, Robes', 'PROP-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-002-03', 'Savoy Suite', 'Luxurious two-bedroom suite with panoramic river views and private balcony.', 5, 28000000, 7, 'Two Bedrooms, Living Room, Balcony, Thames View, Butler Service, Dining Area', 'PROP-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for Claridge's
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-003-01', 'Standard Room', 'Elegant room with classic Mayfair charm and modern amenities.', 2, 100000, 1, 'King Bed, Marble Bathroom, WiFi, Smart TV, Mini Bar, Air Conditioning', 'PROP-003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-003-02', 'Deluxe Room', 'Spacious room with premium furnishings and Hyde Park views.', 2, 150000, 2, 'King Bed, City View, Marble Bathroom, WiFi, Smart TV, Premium Mini Bar', 'PROP-003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-003-03', 'Junior Suite', 'Room with separate living area and exceptional views.', 3, 250000, 3, 'King Bed, Seating Area, Marble Bathroom, WiFi, Nespresso Machine, Robes', 'PROP-003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-003-04', 'Mayfair Suite', 'Luxurious suite with stunning décor and private services.', 4, 450000, 4, 'Master Bedroom, Living Room, Marble Bathroom, Butler Service, WiFi, Dining Area', 'PROP-003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-003-05', 'Penthouse Suite', 'Ultimate luxury with panoramic London views and exclusive amenities.', 6, 850000, 5, 'Two Bedrooms, Grand Living Room, Private Terrace, 360° View, Butler Service, Spa', 'PROP-003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for Shangri-La Hotel at The Shard
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-004-01', 'Deluxe City View', 'Modern luxury room with panoramic city views, marble bathroom, and smart technology.', 2, 12000000, 34, 'King Bed, City View, Marble Bathroom, WiFi, Smart TV, Nespresso Machine', 'PROP-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-004-02', 'Premium Suite', 'Spacious suite with separate living area and breathtaking London skyline views.', 3, 25000000, 40, 'King Bed, Living Room, Skyline View, Walk-in Closet, Spa Bath, Premium Amenities', 'PROP-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-004-03', 'Luxury Penthouse', 'Ultimate luxury penthouse with private terrace overlooking all of London.', 4, 55000000, 52, 'Two Bedrooms, Grand Living Room, Private Terrace, 360° View, Butler Service', 'PROP-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for Hôtel Ritz Paris
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-005-01', 'Deluxe Room', 'Refined room with period furniture, silk fabrics, and marble bathroom.', 2, 9500000, 2, 'King Bed, Silk Fabrics, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'PROP-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-005-02', 'Prestige Suite', 'Elegant suite with Louis XV furniture, separate living room, and garden views.', 3, 18000000, 3, 'King Bed, Living Room, Garden View, Marble Bathroom, Butler Service', 'PROP-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-005-03', 'Imperial Suite', 'Magnificent suite with grand salon, private terrace overlooking Place Vendôme.', 4, 45000000, 5, 'King Bed, Grand Salon, Private Terrace, Jacuzzi, Butler Service, Dining Room', 'PROP-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for Le Meurice
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-006-01', 'Deluxe Room', 'Elegant room overlooking Tuileries Garden with classic French décor.', 2, 8500000, 2, 'King Bed, Garden View, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'PROP-006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-006-02', 'Junior Suite', 'Spacious suite with living area and garden views of the Tuileries.', 3, 16500000, 3, 'King Bed, Living Room, Garden View, Marble Bathroom, Butler Service', 'PROP-006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-006-03', 'Suite Prestige', 'Luxurious suite with panoramic views, private terrace, and premium amenities.', 4, 32500000, 5, 'King Bed, Living Room, Private Terrace, Garden View, Jacuzzi, Butler Service', 'PROP-006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for Hôtel Plaza Athénée
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-007-01', 'Superior Room', 'Refined room on Avenue Montaigne with classic Parisian elegance.', 2, 9000000, 2, 'King Bed, Marble Bathroom, WiFi, Smart TV, Mini Bar, Safe', 'PROP-007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-007-02', 'Deluxe Suite', 'Spacious suite with separate living area and Avenue Montaigne views.', 3, 19500000, 4, 'King Bed, Living Room, City View, Marble Bathroom, Premium Amenities', 'PROP-007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-007-03', 'Presidential Suite', 'Ultimate luxury penthouse with stunning Parisian views and private terrace.', 5, 48500000, 7, 'Two Bedrooms, Grand Living Room, Private Terrace, 360° View, Spa, Butler Service', 'PROP-007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Room Types for Four Seasons George V
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-008-01', 'Superior Room', 'Elegant room with Louis XVI furnishings, marble bathroom, and courtyard views.', 2, 10500000, 2, 'King Bed, Louis XVI Style, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'PROP-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-008-02', 'Premier Suite', 'Luxurious suite with separate living area and stunning Paris views.', 3, 22000000, 4, 'King Bed, Living Room, City View, Walk-in Closet, Marble Bathroom', 'PROP-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-008-03', 'Penthouse Suite', 'Ultimate luxury penthouse with private terrace, panoramic Paris views, and spa.', 6, 65000000, 8, 'Two Bedrooms, Private Terrace, Panoramic View, Private Spa, Butler Service', 'PROP-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_type_id) DO NOTHING;

-- Insert Rooms for The Ritz London
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-001-001', 'Deluxe 201', 1, 0, 'RT-001-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-002', 'Deluxe 202', 1, 0, 'RT-001-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-003', 'Deluxe 203', 1, 0, 'RT-001-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-004', 'Executive 401', 1, 0, 'RT-001-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-005', 'Executive 402', 1, 0, 'RT-001-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-006', 'Royal 601', 1, 0, 'RT-001-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for The Savoy
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-002-001', 'Superior 301', 1, 0, 'RT-002-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-002', 'Superior 302', 1, 0, 'RT-002-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-003', 'Superior 303', 1, 0, 'RT-002-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-004', 'Junior 501', 1, 0, 'RT-002-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-005', 'Junior 502', 1, 0, 'RT-002-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-006', 'Savoy 701', 1, 0, 'RT-002-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for Claridge's (5 room types x 10 rooms = 50 total)
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
-- Standard Rooms (10 rooms)
('PROP-003-101', 'Standard 101', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-102', 'Standard 102', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-103', 'Standard 103', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-104', 'Standard 104', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-105', 'Standard 105', 1, 0, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-106', 'Standard 106', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-107', 'Standard 107', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-108', 'Standard 108', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-109', 'Standard 109', 1, 0, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-110', 'Standard 110', 1, 1, 'RT-003-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Deluxe Rooms (10 rooms)
('PROP-003-201', 'Deluxe 201', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-202', 'Deluxe 202', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-203', 'Deluxe 203', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-204', 'Deluxe 204', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-205', 'Deluxe 205', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-206', 'Deluxe 206', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-207', 'Deluxe 207', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-208', 'Deluxe 208', 1, 0, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-209', 'Deluxe 209', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-210', 'Deluxe 210', 1, 1, 'RT-003-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Junior Suites (10 rooms)
('PROP-003-301', 'Junior Suite 301', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-302', 'Junior Suite 302', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-303', 'Junior Suite 303', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-304', 'Junior Suite 304', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-305', 'Junior Suite 305', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-306', 'Junior Suite 306', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-307', 'Junior Suite 307', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-308', 'Junior Suite 308', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-309', 'Junior Suite 309', 1, 1, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-310', 'Junior Suite 310', 1, 0, 'RT-003-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Mayfair Suites (10 rooms)
('PROP-003-401', 'Mayfair Suite 401', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-402', 'Mayfair Suite 402', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-403', 'Mayfair Suite 403', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-404', 'Mayfair Suite 404', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-405', 'Mayfair Suite 405', 1, 0, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-406', 'Mayfair Suite 406', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-407', 'Mayfair Suite 407', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-408', 'Mayfair Suite 408', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-409', 'Mayfair Suite 409', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-410', 'Mayfair Suite 410', 1, 1, 'RT-003-04', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Penthouse Suites (10 rooms)
('PROP-003-501', 'Penthouse 501', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-502', 'Penthouse 502', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-503', 'Penthouse 503', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-504', 'Penthouse 504', 1, 0, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-505', 'Penthouse 505', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-506', 'Penthouse 506', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-507', 'Penthouse 507', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-508', 'Penthouse 508', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-509', 'Penthouse 509', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003-510', 'Penthouse 510', 1, 1, 'RT-003-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for Shangri-La Hotel at The Shard
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-004-001', 'Deluxe 3401', 1, 1, 'RT-004-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-002', 'Deluxe 3402', 1, 1, 'RT-004-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-003', 'Deluxe 3403', 1, 0, 'RT-004-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-004', 'Premium 4001', 1, 1, 'RT-004-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-005', 'Premium 4002', 1, 1, 'RT-004-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-006', 'Penthouse 5201', 1, 0, 'RT-004-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for Hôtel Ritz Paris
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-005-001', 'Deluxe 201', 1, 0, 'RT-005-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-002', 'Deluxe 202', 1, 0, 'RT-005-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-003', 'Deluxe 203', 1, 0, 'RT-005-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-004', 'Prestige 301', 1, 0, 'RT-005-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-005', 'Prestige 302', 1, 0, 'RT-005-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-006', 'Imperial 501', 1, 0, 'RT-005-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for Four Seasons George V
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-008-001', 'Superior 201', 1, 0, 'RT-008-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-002', 'Superior 202', 1, 0, 'RT-008-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-003', 'Superior 203', 1, 0, 'RT-008-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-004', 'Premier 401', 1, 0, 'RT-008-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-005', 'Premier 402', 1, 0, 'RT-008-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-006', 'Penthouse 801', 1, 0, 'RT-008-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for Le Meurice (3 room types x 10 rooms = 30 total)
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
-- Deluxe Rooms (10 rooms)
('ROOM-006-001', 'Deluxe 301', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-002', 'Deluxe 302', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-003', 'Deluxe 303', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-004', 'Deluxe 304', 1, 0, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-005', 'Deluxe 305', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-006', 'Deluxe 306', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-007', 'Deluxe 307', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-008', 'Deluxe 308', 1, 0, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-009', 'Deluxe 309', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-010', 'Deluxe 310', 1, 1, 'RT-006-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Junior Suites (10 rooms)
('ROOM-006-011', 'Junior 401', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-012', 'Junior 402', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-013', 'Junior 403', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-014', 'Junior 404', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-015', 'Junior 405', 1, 0, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-016', 'Junior 406', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-017', 'Junior 407', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-018', 'Junior 408', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-019', 'Junior 409', 1, 0, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-020', 'Junior 410', 1, 1, 'RT-006-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Suite Prestige (10 rooms)
('ROOM-006-021', 'Prestige 501', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-022', 'Prestige 502', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-023', 'Prestige 503', 1, 0, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-024', 'Prestige 504', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-025', 'Prestige 505', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-026', 'Prestige 506', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-027', 'Prestige 507', 1, 0, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-028', 'Prestige 508', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-029', 'Prestige 509', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-006-030', 'Prestige 510', 1, 1, 'RT-006-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Rooms for Hôtel Plaza Athénée (3 room types x 10 rooms = 30 total)
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
-- Superior Rooms (10 rooms)
('ROOM-007-001', 'Superior 301', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-002', 'Superior 302', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-003', 'Superior 303', 1, 0, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-004', 'Superior 304', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-005', 'Superior 305', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-006', 'Superior 306', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-007', 'Superior 307', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-008', 'Superior 308', 1, 0, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-009', 'Superior 309', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-010', 'Superior 310', 1, 1, 'RT-007-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Deluxe Suites (10 rooms)
('ROOM-007-011', 'Deluxe 401', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-012', 'Deluxe 402', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-013', 'Deluxe 403', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-014', 'Deluxe 404', 1, 0, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-015', 'Deluxe 405', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-016', 'Deluxe 406', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-017', 'Deluxe 407', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-018', 'Deluxe 408', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-019', 'Deluxe 409', 1, 0, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-020', 'Deluxe 410', 1, 1, 'RT-007-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Presidential Suites (10 rooms)
('ROOM-007-021', 'Presidential 501', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-022', 'Presidential 502', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-023', 'Presidential 503', 1, 0, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-024', 'Presidential 504', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-025', 'Presidential 505', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-026', 'Presidential 506', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-027', 'Presidential 507', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-028', 'Presidential 508', 1, 0, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-029', 'Presidential 509', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-007-030', 'Presidential 510', 1, 1, 'RT-007-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (room_id) DO NOTHING;

-- Insert Bookings
INSERT INTO accommodation_booking (booking_id, customer_id, customer_name, customer_email, customer_phone, room_id, check_in_date, check_out_date, capacity, total_days, total_price, is_breakfast, extra_pay, status, refund, created_date, updated_date) VALUES
('BK-2025-001', '650e8400-e29b-41d4-a716-446655440001', 'James Anderson', 'james.anderson@email.com', '+44 7700 900123', 'ROOM-001-001', '2025-11-10 14:00:00', '2025-11-13 11:00:00', 2, 3, 25500000, true, 0, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-002', '650e8400-e29b-41d4-a716-446655440002', 'Sophie Laurent', 'sophie.laurent@email.fr', '+33 6 12 34 56 78', 'ROOM-005-004', '2025-11-15 15:00:00', '2025-11-18 12:00:00', 2, 3, 54000000, true, 0, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-003', '650e8400-e29b-41d4-a716-446655440003', 'Michael Chen', 'michael.chen@email.com', '+44 7700 900456', 'ROOM-002-004', '2025-11-12 14:00:00', '2025-11-15 11:00:00', 3, 3, 37500000, true, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-004', '650e8400-e29b-41d4-a716-446655440004', 'Isabella Martinez', 'isabella.martinez@email.es', '+34 612 345 678', 'ROOM-008-002', '2025-11-20 15:00:00', '2025-11-25 12:00:00', 2, 5, 52500000, true, 0, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-005', '650e8400-e29b-41d4-a716-446655440005', 'William Thompson', 'william.thompson@email.uk', '+44 7700 900789', 'ROOM-001-006', '2025-11-18 14:00:00', '2025-11-22 11:00:00', 4, 4, 140000000, true, 0, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-006', '650e8400-e29b-41d4-a716-446655440006', 'Emma Dubois', 'emma.dubois@email.fr', '+33 6 98 76 54 32', 'ROOM-005-001', '2025-11-08 15:00:00', '2025-11-11 12:00:00', 2, 3, 28500000, true, 0, 4, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-007', '650e8400-e29b-41d4-a716-446655440007', 'Alexander Brown', 'alex.brown@email.com', '+44 7700 901234', 'ROOM-002-001', '2025-11-25 14:00:00', '2025-11-28 11:00:00', 2, 3, 22500000, true, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-008', '650e8400-e29b-41d4-a716-446655440008', 'Charlotte Rousseau', 'charlotte.rousseau@email.fr', '+33 6 11 22 33 44', 'ROOM-008-006', '2025-11-30 15:00:00', '2025-12-05 12:00:00', 6, 5, 325000000, true, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-009', '650e8400-e29b-41d4-a716-446655440009', 'Oliver Wilson', 'oliver.wilson@email.uk', '+44 7700 902345', 'ROOM-001-004', '2025-11-14 14:00:00', '2025-11-17 11:00:00', 3, 3, 45000000, true, 0, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BK-2025-010', '650e8400-e29b-41d4-a716-446655440010', 'Amélie Bernard', 'amelie.bernard@email.fr', '+33 6 55 66 77 88', 'ROOM-005-006', '2025-12-01 15:00:00', '2025-12-04 12:00:00', 4, 3, 135000000, true, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (booking_id) DO NOTHING;
