export const ROOM_TYPES = {
  hotel: [
    'Single Room',
    'Double Room',
    'Deluxe Room',
    'Superior Room',
    'Suite'
  ],
  villa: [
    'Family Room',
    'Luxury',
    'Beachfront',
    'Mountain',
    'Eco-friendly',
    'Romantic'
  ],
  apartment: [
    'Studio',
    '1BR',
    '2BR',
    '3BR',
    'Penthouse'
  ]
}

export function getRoomTypesByPropertyType(type: number): string[] {
  switch (type) {
    case 1:
      return ROOM_TYPES.hotel
    case 2:
      return ROOM_TYPES.villa
    case 3:
      return ROOM_TYPES.apartment
    default:
      return []
  }
}
