<<<<<<< HEAD
-- Insert Properties (Luxury Hotels in London and Paris)
INSERT INTO property (property_id, property_name, address, province, type, description, owner_id, owner_name, active_status, total_room, income, created_date, updated_date) VALUES
('PROP-001', 'The Ritz London', '150 Piccadilly, St. James''s, London W1J 9BR, United Kingdom', '0', 1, 'Iconic 5-star luxury hotel in the heart of London, offering elegant rooms and world-class service since 1906.', '550e8400-e29b-41d4-a716-446655440001', 'Sir Charles Forte', 1, 136, 125000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-002', 'The Savoy', 'Strand, London WC2R 0EZ, United Kingdom', '0', 1, 'Historic luxury hotel on the banks of the Thames, featuring Art Deco design and Michelin-starred dining.', '550e8400-e29b-41d4-a716-446655440002', 'Fairmont Hotels', 1, 268, 98000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-003', 'Claridge''s', 'Brook Street, Mayfair, London W1K 4HR, United Kingdom', '0', 1, 'Legendary 5-star hotel in Mayfair, renowned for its Art Deco heritage and impeccable British service.', '550e8400-e29b-41d4-a716-446655440003', 'Maybourne Hotel Group', 1, 203, 87500000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-004', 'Shangri-La Hotel at The Shard', '31 St Thomas Street, London SE1 9QU, United Kingdom', '0', 1, 'Ultra-luxury hotel occupying floors 34-52 of The Shard, offering breathtaking panoramic views of London.', '550e8400-e29b-41d4-a716-446655440004', 'Shangri-La Hotels', 1, 202, 112000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-005', 'Hôtel Ritz Paris', '15 Place Vendôme, 75001 Paris, France', '1', 1, 'Legendary Parisian palace hotel, epitome of French luxury and elegance since 1898.', '550e8400-e29b-41d4-a716-446655440005', 'Mohamed Al-Fayed', 1, 142, 145000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-006', 'Le Meurice', '228 Rue de Rivoli, 75001 Paris, France', '1', 1, 'Palace hotel facing the Tuileries Garden, combining 18th-century opulence with contemporary luxury.', '550e8400-e29b-41d4-a716-446655440006', 'Dorchester Collection', 1, 160, 92000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-007', 'Hôtel Plaza Athénée', '25 Avenue Montaigne, 75008 Paris, France', '1', 1, 'Iconic palace hotel on Avenue Montaigne, embodying Parisian haute couture and culinary excellence.', '550e8400-e29b-41d4-a716-446655440007', 'Dorchester Collection', 1, 208, 135000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-008', 'Four Seasons Hotel George V', '31 Avenue George V, 75008 Paris, France', '1', 1, 'Legendary palace hotel steps from the Champs-Élysées, featuring three Michelin-starred restaurants.', '550e8400-e29b-41d4-a716-446655440008', 'Four Seasons Hotels', 1, 244, 156000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-009', 'The Connaught', 'Carlos Place, Mayfair, London W1K 2AL, United Kingdom', '0', 1, 'Quintessentially British luxury hotel in Mayfair, blending Edwardian elegance with contemporary style.', '550e8400-e29b-41d4-a716-446655440009', 'Maybourne Hotel Group', 1, 121, 78000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PROP-010', 'Mandarin Oriental Hyde Park', '66 Knightsbridge, London SW1X 7LA, United Kingdom', '0', 1, 'Elegant Victorian hotel overlooking Hyde Park, offering award-winning spa and fine dining.', '550e8400-e29b-41d4-a716-446655440010', 'Mandarin Oriental Group', 1, 181, 95000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for The Ritz London
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-001-01', 'Deluxe Room', 'Elegant room with classic Louis XVI décor, marble bathroom, and luxurious amenities.', 2, 8500000, 2, 'King Bed, Marble Bathroom, WiFi, Smart TV, Mini Bar, Safe, Air Conditioning', 'PROP-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-001-02', 'Executive Suite', 'Spacious suite with separate living area, stunning views, and premium furnishings.', 3, 15000000, 4, 'King Bed, Separate Living Room, Marble Bathroom, Butler Service, WiFi, Smart TV', 'PROP-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-001-03', 'Royal Suite', 'Opulent suite with magnificent décor, private terrace, and personalized butler service.', 4, 35000000, 6, 'King Bed, Living Room, Dining Area, Private Terrace, Butler Service, Jacuzzi', 'PROP-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for The Savoy
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-002-01', 'Superior Queen Room', 'Art Deco styled room with Thames or courtyard views, featuring bespoke furnishings.', 2, 7500000, 3, 'Queen Bed, Art Deco Design, WiFi, Smart TV, Mini Bar, Rainfall Shower', 'PROP-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-002-02', 'Junior Suite', 'Spacious suite with separate seating area and views of the Thames or London skyline.', 3, 12500000, 5, 'King Bed, Seating Area, Marble Bathroom, WiFi, Nespresso Machine, Robes', 'PROP-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-002-03', 'Savoy Suite', 'Luxurious two-bedroom suite with panoramic river views and private balcony.', 5, 28000000, 7, 'Two Bedrooms, Living Room, Balcony, Thames View, Butler Service, Dining Area', 'PROP-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for Shangri-La Hotel at The Shard
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-004-01', 'Deluxe City View', 'Modern luxury room with panoramic city views, marble bathroom, and smart technology.', 2, 12000000, 34, 'King Bed, City View, Marble Bathroom, WiFi, Smart TV, Nespresso Machine', 'PROP-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-004-02', 'Premium Suite', 'Spacious suite with separate living area and breathtaking London skyline views.', 3, 25000000, 40, 'King Bed, Living Room, Skyline View, Walk-in Closet, Spa Bath, Premium Amenities', 'PROP-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-004-03', 'Luxury Penthouse', 'Ultimate luxury penthouse with private terrace overlooking all of London.', 4, 55000000, 52, 'Two Bedrooms, Grand Living Room, Private Terrace, 360° View, Butler Service', 'PROP-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for Hôtel Ritz Paris
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-005-01', 'Deluxe Room', 'Refined room with period furniture, silk fabrics, and marble bathroom.', 2, 9500000, 2, 'King Bed, Silk Fabrics, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'PROP-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-005-02', 'Prestige Suite', 'Elegant suite with Louis XV furniture, separate living room, and garden views.', 3, 18000000, 3, 'King Bed, Living Room, Garden View, Marble Bathroom, Butler Service', 'PROP-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-005-03', 'Imperial Suite', 'Magnificent suite with grand salon, private terrace overlooking Place Vendôme.', 4, 45000000, 5, 'King Bed, Grand Salon, Private Terrace, Jacuzzi, Butler Service, Dining Room', 'PROP-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for Four Seasons George V
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, property_id, created_date, updated_date) VALUES
('RT-008-01', 'Superior Room', 'Elegant room with Louis XVI furnishings, marble bathroom, and courtyard views.', 2, 10500000, 2, 'King Bed, Louis XVI Style, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'PROP-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-008-02', 'Premier Suite', 'Luxurious suite with separate living area and stunning Paris views.', 3, 22000000, 4, 'King Bed, Living Room, City View, Walk-in Closet, Marble Bathroom', 'PROP-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-008-03', 'Penthouse Suite', 'Ultimate luxury penthouse with private terrace, panoramic Paris views, and spa.', 6, 65000000, 8, 'Two Bedrooms, Private Terrace, Panoramic View, Private Spa, Butler Service', 'PROP-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
=======
-- Insert HOTerties (Luxury Hotels in London and Paris)
INSERT INTO HOTerty (HOTerty_id, HOTerty_name, address, province, type, description, owner_id, owner_name, active_status, total_room, income, created_date, updated_date) VALUES
('HOT-0001-001', 'The Ritz London', '150 Piccadilly, St. James''s, London W1J 9BR, United Kingdom', '0', 1, 'Iconic 5-star luxury hotel in the heart of London, offering elegant rooms and world-class service since 1906.', '550e8400-e29b-41d4-a716-446655440001', 'Sir Charles Forte', 1, 136, 125000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0002-002', 'The Savoy', 'Strand, London WC2R 0EZ, United Kingdom', '0', 1, 'Historic luxury hotel on the banks of the Thames, featuring Art Deco design and Michelin-starred dining.', '550e8400-e29b-41d4-a716-446655440002', 'Fairmont Hotels', 1, 268, 98000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0003-003', 'Claridge''s', 'Brook Street, Mayfair, London W1K 4HR, United Kingdom', '0', 1, 'Legendary 5-star hotel in Mayfair, renowned for its Art Deco heritage and impeccable British service.', '550e8400-e29b-41d4-a716-446655440003', 'Maybourne Hotel Group', 1, 203, 87500000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0004-004', 'Shangri-La Hotel at The Shard', '31 St Thomas Street, London SE1 9QU, United Kingdom', '0', 1, 'Ultra-luxury hotel occupying floors 34-52 of The Shard, offering breathtaking panoramic views of London.', '550e8400-e29b-41d4-a716-446655440004', 'Shangri-La Hotels', 1, 202, 112000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0005-005', 'Hôtel Ritz Paris', '15 Place Vendôme, 75001 Paris, France', '1', 1, 'Legendary Parisian palace hotel, epitome of French luxury and elegance since 1898.', '550e8400-e29b-41d4-a716-446655440005', 'Mohamed Al-Fayed', 1, 142, 145000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0006-006', 'Le Meurice', '228 Rue de Rivoli, 75001 Paris, France', '1', 1, 'Palace hotel facing the Tuileries Garden, combining 18th-century opulence with contemporary luxury.', '550e8400-e29b-41d4-a716-446655440006', 'Dorchester Collection', 1, 160, 92000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0007-007', 'Hôtel Plaza Athénée', '25 Avenue Montaigne, 75008 Paris, France', '1', 1, 'Iconic palace hotel on Avenue Montaigne, embodying Parisian haute couture and culinary excellence.', '550e8400-e29b-41d4-a716-446655440007', 'Dorchester Collection', 1, 208, 135000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0008-008', 'Four Seasons Hotel George V', '31 Avenue George V, 75008 Paris, France', '1', 1, 'Legendary palace hotel steps from the Champs-Élysées, featuring three Michelin-starred restaurants.', '550e8400-e29b-41d4-a716-446655440008', 'Four Seasons Hotels', 1, 244, 156000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0009-009', 'The Connaught', 'Carlos Place, Mayfair, London W1K 2AL, United Kingdom', '0', 1, 'Quintessentially British luxury hotel in Mayfair, blending Edwardian elegance with contemporary style.', '550e8400-e29b-41d4-a716-446655440009', 'Maybourne Hotel Group', 1, 121, 78000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HOT-0010-010', 'Mandarin Oriental Hyde Park', '66 Knightsbridge, London SW1X 7LA, United Kingdom', '0', 1, 'Elegant Victorian hotel overlooking Hyde Park, offering award-winning spa and fine dining.', '550e8400-e29b-41d4-a716-446655440010', 'Mandarin Oriental Group', 1, 181, 95000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for The Ritz London
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, HOTerty_id, created_date, updated_date) VALUES
('RT-001-01', 'Deluxe Room', 'Elegant room with classic Louis XVI décor, marble bathroom, and luxurious amenities.', 2, 8500000, 2, 'King Bed, Marble Bathroom, WiFi, Smart TV, Mini Bar, Safe, Air Conditioning', 'HOT-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-001-02', 'Executive Suite', 'Spacious suite with separate living area, stunning views, and premium furnishings.', 3, 15000000, 4, 'King Bed, Separate Living Room, Marble Bathroom, Butler Service, WiFi, Smart TV', 'HOT-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-001-03', 'Royal Suite', 'Opulent suite with magnificent décor, private terrace, and personalized butler service.', 4, 35000000, 6, 'King Bed, Living Room, Dining Area, Private Terrace, Butler Service, Jacuzzi', 'HOT-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for The Savoy
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, HOTerty_id, created_date, updated_date) VALUES
('RT-002-01', 'Superior Queen Room', 'Art Deco styled room with Thames or courtyard views, featuring bespoke furnishings.', 2, 7500000, 3, 'Queen Bed, Art Deco Design, WiFi, Smart TV, Mini Bar, Rainfall Shower', 'HOT-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-002-02', 'Junior Suite', 'Spacious suite with separate seating area and views of the Thames or London skyline.', 3, 12500000, 5, 'King Bed, Seating Area, Marble Bathroom, WiFi, Nespresso Machine, Robes', 'HOT-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-002-03', 'Savoy Suite', 'Luxurious two-bedroom suite with panoramic river views and private balcony.', 5, 28000000, 7, 'Two Bedrooms, Living Room, Balcony, Thames View, Butler Service, Dining Area', 'HOT-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for Shangri-La Hotel at The Shard
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, HOTerty_id, created_date, updated_date) VALUES
('RT-004-01', 'Deluxe City View', 'Modern luxury room with panoramic city views, marble bathroom, and smart technology.', 2, 12000000, 34, 'King Bed, City View, Marble Bathroom, WiFi, Smart TV, Nespresso Machine', 'HOT-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-004-02', 'Premium Suite', 'Spacious suite with separate living area and breathtaking London skyline views.', 3, 25000000, 40, 'King Bed, Living Room, Skyline View, Walk-in Closet, Spa Bath, Premium Amenities', 'HOT-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-004-03', 'Luxury Penthouse', 'Ultimate luxury penthouse with private terrace overlooking all of London.', 4, 55000000, 52, 'Two Bedrooms, Grand Living Room, Private Terrace, 360° View, Butler Service', 'HOT-004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for Hôtel Ritz Paris
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, HOTerty_id, created_date, updated_date) VALUES
('RT-005-01', 'Deluxe Room', 'Refined room with period furniture, silk fabrics, and marble bathroom.', 2, 9500000, 2, 'King Bed, Silk Fabrics, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'HOT-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-005-02', 'Prestige Suite', 'Elegant suite with Louis XV furniture, separate living room, and garden views.', 3, 18000000, 3, 'King Bed, Living Room, Garden View, Marble Bathroom, Butler Service', 'HOT-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-005-03', 'Imperial Suite', 'Magnificent suite with grand salon, private terrace overlooking Place Vendôme.', 4, 45000000, 5, 'King Bed, Grand Salon, Private Terrace, Jacuzzi, Butler Service, Dining Room', 'HOT-005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Room Types for Four Seasons George V
INSERT INTO room_type (room_type_id, name, description, capacity, price, floor, facility, HOTerty_id, created_date, updated_date) VALUES
('RT-008-01', 'Superior Room', 'Elegant room with Louis XVI furnishings, marble bathroom, and courtyard views.', 2, 10500000, 2, 'King Bed, Louis XVI Style, Marble Bathroom, WiFi, Smart TV, Mini Bar', 'HOT-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-008-02', 'Premier Suite', 'Luxurious suite with separate living area and stunning Paris views.', 3, 22000000, 4, 'King Bed, Living Room, City View, Walk-in Closet, Marble Bathroom', 'HOT-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RT-008-03', 'Penthouse Suite', 'Ultimate luxury penthouse with private terrace, panoramic Paris views, and spa.', 6, 65000000, 8, 'Two Bedrooms, Private Terrace, Panoramic View, Private Spa, Butler Service', 'HOT-008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
>>>>>>> dfbbe78c2681703f7827fa590ee09f0af63b861e

-- Insert Rooms for The Ritz London
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-001-001', 'Deluxe 201', 1, 0, 'RT-001-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-002', 'Deluxe 202', 1, 0, 'RT-001-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-003', 'Deluxe 203', 1, 0, 'RT-001-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-004', 'Executive 401', 1, 0, 'RT-001-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-005', 'Executive 402', 1, 0, 'RT-001-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-001-006', 'Royal 601', 1, 0, 'RT-001-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Rooms for The Savoy
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-002-001', 'Superior 301', 1, 0, 'RT-002-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-002', 'Superior 302', 1, 0, 'RT-002-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-003', 'Superior 303', 1, 0, 'RT-002-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-004', 'Junior 501', 1, 0, 'RT-002-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-005', 'Junior 502', 1, 0, 'RT-002-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-002-006', 'Savoy 701', 1, 0, 'RT-002-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Rooms for Shangri-La Hotel at The Shard
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-004-001', 'Deluxe 3401', 1, 1, 'RT-004-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-002', 'Deluxe 3402', 1, 1, 'RT-004-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-003', 'Deluxe 3403', 1, 0, 'RT-004-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-004', 'Premium 4001', 1, 1, 'RT-004-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-005', 'Premium 4002', 1, 1, 'RT-004-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-004-006', 'Penthouse 5201', 1, 0, 'RT-004-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Rooms for Hôtel Ritz Paris
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-005-001', 'Deluxe 201', 1, 0, 'RT-005-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-002', 'Deluxe 202', 1, 0, 'RT-005-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-003', 'Deluxe 203', 1, 0, 'RT-005-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-004', 'Prestige 301', 1, 0, 'RT-005-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-005', 'Prestige 302', 1, 0, 'RT-005-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-005-006', 'Imperial 501', 1, 0, 'RT-005-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Rooms for Four Seasons George V
INSERT INTO room (room_id, name, active_room, availability_status, room_type_id, created_date, updated_date) VALUES
('ROOM-008-001', 'Superior 201', 1, 0, 'RT-008-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-002', 'Superior 202', 1, 0, 'RT-008-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-003', 'Superior 203', 1, 0, 'RT-008-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-004', 'Premier 401', 1, 0, 'RT-008-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-005', 'Premier 402', 1, 0, 'RT-008-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROOM-008-006', 'Penthouse 801', 1, 0, 'RT-008-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
('BK-2025-010', '650e8400-e29b-41d4-a716-446655440010', 'Amélie Bernard', 'amelie.bernard@email.fr', '+33 6 55 66 77 88', 'ROOM-005-006', '2025-12-01 15:00:00', '2025-12-04 12:00:00', 4, 3, 135000000, true, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

<<<<<<< HEAD
-- Update property income based on completed bookings
UPDATE property SET income = 25500000 WHERE property_id = 'PROP-001';
UPDATE property SET income = 37500000 WHERE property_id = 'PROP-002';
UPDATE property SET income = 0 WHERE property_id = 'PROP-004';
UPDATE property SET income = 82500000 WHERE property_id = 'PROP-005';
UPDATE property SET income = 52500000 WHERE property_id = 'PROP-008';

-- Update total_room counts
UPDATE property SET total_room = 6 WHERE property_id = 'PROP-001';
UPDATE property SET total_room = 6 WHERE property_id = 'PROP-002';
UPDATE property SET total_room = 6 WHERE property_id = 'PROP-004';
UPDATE property SET total_room = 6 WHERE property_id = 'PROP-005';
UPDATE property SET total_room = 6 WHERE property_id = 'PROP-008';
=======
-- Update HOTerty income based on completed bookings
UPDATE HOTerty SET income = 25500000 WHERE HOTerty_id = 'HOT-001';
UPDATE HOTerty SET income = 37500000 WHERE HOTerty_id = 'HOT-002';
UPDATE HOTerty SET income = 0 WHERE HOTerty_id = 'HOT-004';
UPDATE HOTerty SET income = 82500000 WHERE HOTerty_id = 'HOT-005';
UPDATE HOTerty SET income = 52500000 WHERE HOTerty_id = 'HOT-008';

-- Update total_room counts
UPDATE HOTerty SET total_room = 6 WHERE HOTerty_id = 'HOT-001';
UPDATE HOTerty SET total_room = 6 WHERE HOTerty_id = 'HOT-002';
UPDATE HOTerty SET total_room = 6 WHERE HOTerty_id = 'HOT-004';
UPDATE HOTerty SET total_room = 6 WHERE HOTerty_id = 'HOT-005';
UPDATE HOTerty SET total_room = 6 WHERE HOTerty_id = 'HOT-008';
>>>>>>> dfbbe78c2681703f7827fa590ee09f0af63b861e
