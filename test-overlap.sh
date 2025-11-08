#!/bin/bash

echo "🧪 =========================================="
echo "🧪 TESTING BOOKING OVERLAP DETECTION"
echo "🧪 =========================================="

ROOM_ID="PROP-003-101"  # Room 101 at Claridge's

echo ""
echo "📝 Step 1: Create first booking (14-15 Nov)"
echo "   Room: $ROOM_ID"
echo "   Dates: 2025-11-14 to 2025-11-15"
echo ""

BOOKING1=$(curl -s -X POST "http://localhost:8080/api/booking/create" \
  -H "Content-Type: application/json" \
  -d '{
    "roomID": "'$ROOM_ID'",
    "checkInDate": "2025-11-14T14:00:00",
    "checkOutDate": "2025-11-15T12:00:00",
    "customerID": "550e8400-e29b-41d4-a716-446655440000",
    "customerName": "Overlap Test User 1",
    "customerEmail": "overlap1@test.com",
    "customerPhone": "081111111111",
    "isBreakfast": false,
    "capacity": 1
  }')

BOOKING1_ID=$(echo $BOOKING1 | python3 -c "import sys, json; print(json.load(sys.stdin)['data']['bookingID'])" 2>/dev/null)

if [ -z "$BOOKING1_ID" ]; then
  echo "❌ Failed to create first booking"
  echo "$BOOKING1" | python3 -m json.tool
  exit 1
fi

echo "✅ First booking created: $BOOKING1_ID"

echo ""
echo "💳 Step 2: Confirm first booking"
curl -s -X PUT "http://localhost:8080/api/booking/confirm/$BOOKING1_ID" > /dev/null
echo "✅ First booking confirmed (Status = 1)"

echo ""
echo "🔍 Step 3: Check room availability for EXACT same dates (14-15)"
echo "   Expected: NOT available (false)"
echo ""

AVAIL1=$(curl -s "http://localhost:8080/api/property/room/$ROOM_ID/available?checkIn=2025-11-14T14:00:00&checkOut=2025-11-15T12:00:00")
AVAILABLE1=$(echo $AVAIL1 | python3 -c "import sys, json; print(json.load(sys.stdin)['data'])" 2>/dev/null)

echo "Response: $AVAIL1" | python3 -m json.tool 2>/dev/null
echo ""
if [ "$AVAILABLE1" = "False" ] || [ "$AVAILABLE1" = "false" ]; then
  echo "✅ CORRECT: Room shows as NOT available for 14-15"
else
  echo "❌ WRONG: Room shows as available but should be booked!"
fi

echo ""
echo "🔍 Step 4: Check room availability for OVERLAPPING dates (14-16)"
echo "   Existing booking: 14-15"
echo "   New request: 14-16 (overlaps 14-15)"
echo "   Expected: NOT available (false)"
echo ""

AVAIL2=$(curl -s "http://localhost:8080/api/property/room/$ROOM_ID/available?checkIn=2025-11-14T14:00:00&checkOut=2025-11-16T12:00:00")
AVAILABLE2=$(echo $AVAIL2 | python3 -c "import sys, json; print(json.load(sys.stdin)['data'])" 2>/dev/null)

echo "Response: $AVAIL2" | python3 -m json.tool 2>/dev/null
echo ""
if [ "$AVAILABLE2" = "False" ] || [ "$AVAILABLE2" = "false" ]; then
  echo "✅ CORRECT: Room shows as NOT available for 14-16 (overlap detected)"
else
  echo "❌ WRONG: Room shows as available but overlaps with existing booking!"
  echo ""
  echo "🐛 BUG CONFIRMED: Overlap detection not working correctly"
fi

echo ""
echo "🔍 Step 5: Check room availability for NON-OVERLAPPING dates (16-17)"
echo "   Existing booking: 14-15"
echo "   New request: 16-17 (no overlap)"
echo "   Expected: Available (true)"
echo ""

AVAIL3=$(curl -s "http://localhost:8080/api/property/room/$ROOM_ID/available?checkIn=2025-11-16T14:00:00&checkOut=2025-11-17T12:00:00")
AVAILABLE3=$(echo $AVAIL3 | python3 -c "import sys, json; print(json.load(sys.stdin)['data'])" 2>/dev/null)

echo "Response: $AVAIL3" | python3 -m json.tool 2>/dev/null
echo ""
if [ "$AVAILABLE3" = "True" ] || [ "$AVAILABLE3" = "true" ]; then
  echo "✅ CORRECT: Room shows as available for 16-17 (no overlap)"
else
  echo "⚠️  Room shows as NOT available, but should be available"
fi

echo ""
echo "🧹 Cleanup: Cancelling test booking"
curl -s -X PUT "http://localhost:8080/api/booking/cancel/$BOOKING1_ID" > /dev/null
echo "✅ Test booking cancelled"

echo ""
echo "🧪 =========================================="
echo "🧪 TEST COMPLETED"
echo "🧪 =========================================="
