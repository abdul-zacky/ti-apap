#!/bin/bash

# Test Refund Feature
# This script creates a booking, confirms payment, then updates it with lower price to trigger refund

BASE_URL="http://localhost:8080/api"
CUSTOMER_ID="550e8400-e29b-41d4-a716-446655440001"

echo "🧪 =========================================="
echo "🧪 TESTING REFUND FEATURE"
echo "🧪 =========================================="
echo ""

# Step 1: Create booking with expensive room (Deluxe Room)
echo "📝 Step 1: Creating booking with Deluxe Room (expensive)"
echo "   - Room: Deluxe 202 (PROP-003-202)"
echo "   - Price: Rp 150,000/night"
echo "   - Dates: 20-21 Nov 2025 (1 night)"
echo "   - Breakfast: Yes (Rp 50,000)"
echo "   - Total: Rp 200,000"
echo ""

CREATE_RESPONSE=$(curl -s -X POST "${BASE_URL}/booking/create" \
  -H "Content-Type: application/json" \
  -d '{
    "roomID": "PROP-003-202",
    "checkInDate": "2025-11-20T14:00:00",
    "checkOutDate": "2025-11-21T12:00:00",
    "customerID": "'${CUSTOMER_ID}'",
    "customerName": "Refund Test User",
    "customerEmail": "refund.test@example.com",
    "customerPhone": "081234567890",
    "isBreakfast": true,
    "capacity": 2
  }')

echo "Response:"
echo "$CREATE_RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$CREATE_RESPONSE"
echo ""

# Extract booking ID
BOOKING_ID=$(echo "$CREATE_RESPONSE" | python3 -c "import sys, json; print(json.load(sys.stdin).get('data', {}).get('bookingID', ''))" 2>/dev/null)

if [ -z "$BOOKING_ID" ]; then
  echo "❌ Failed to create booking. Exiting..."
  exit 1
fi

echo "✅ Booking created successfully!"
echo "   Booking ID: $BOOKING_ID"
echo ""

# Wait a bit
sleep 2

# Step 2: Confirm payment
echo "💳 Step 2: Confirming payment for booking $BOOKING_ID"
echo ""

CONFIRM_RESPONSE=$(curl -s -X PUT "${BASE_URL}/booking/confirm/${BOOKING_ID}")

echo "Response:"
echo "$CONFIRM_RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$CONFIRM_RESPONSE"
echo ""
echo "✅ Payment confirmed! Status = Confirmed (1)"
echo ""

# Wait a bit
sleep 2

# Step 3: Update booking with cheaper room (Standard Room, no breakfast)
echo "🔄 Step 3: Updating booking to Standard Room (cheaper)"
echo "   - Room: Standard 101 (PROP-003-101)"
echo "   - Price: Rp 100,000/night"
echo "   - Dates: Same (20-21 Nov 2025)"
echo "   - Breakfast: No"
echo "   - Total: Rp 100,000"
echo "   - Expected Refund: Rp 200,000 - Rp 100,000 = Rp 100,000"
echo ""

UPDATE_RESPONSE=$(curl -s -X PUT "${BASE_URL}/booking/update/${BOOKING_ID}" \
  -H "Content-Type: application/json" \
  -d '{
    "roomID": "PROP-003-101",
    "checkInDate": "2025-11-20T14:00:00",
    "checkOutDate": "2025-11-21T12:00:00",
    "customerID": "'${CUSTOMER_ID}'",
    "customerName": "Refund Test User",
    "customerEmail": "refund.test@example.com",
    "customerPhone": "081234567890",
    "isBreakfast": false,
    "capacity": 1
  }')

echo "Response:"
echo "$UPDATE_RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$UPDATE_RESPONSE"
echo ""

# Extract extraPay value
EXTRA_PAY=$(echo "$UPDATE_RESPONSE" | python3 -c "import sys, json; print(json.load(sys.stdin).get('data', {}).get('extraPay', 0))" 2>/dev/null)

echo "💰 =========================================="
if [ "$EXTRA_PAY" -lt 0 ]; then
  REFUND_AMOUNT=$(echo "$EXTRA_PAY * -1" | bc 2>/dev/null || echo ${EXTRA_PAY#-})
  echo "✅ REFUND TRIGGERED!"
  echo "   Refund Amount: Rp $(printf "%'d" $REFUND_AMOUNT 2>/dev/null || echo $REFUND_AMOUNT)"
  echo "   extraPay value: $EXTRA_PAY (negative = refund)"
elif [ "$EXTRA_PAY" -gt 0 ]; then
  echo "⚠️  EXTRA PAY REQUIRED"
  echo "   Extra Payment: Rp $(printf "%'d" $EXTRA_PAY 2>/dev/null || echo $EXTRA_PAY)"
  echo "   extraPay value: $EXTRA_PAY (positive = extra pay)"
else
  echo "ℹ️  NO CHANGE IN PRICE"
  echo "   extraPay value: $EXTRA_PAY"
fi
echo "💰 =========================================="
echo ""

# Step 4: Get full booking details to verify
echo "📋 Step 4: Fetching updated booking details"
echo ""

DETAIL_RESPONSE=$(curl -s -X GET "${BASE_URL}/booking/${BOOKING_ID}")

echo "Final Booking Details:"
echo "$DETAIL_RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$DETAIL_RESPONSE"
echo ""

echo "🧪 =========================================="
echo "🧪 TEST COMPLETED"
echo "🧪 =========================================="
echo ""
echo "📱 Now test in frontend:"
echo "   1. Go to Bookings page (http://localhost:5173/bookings)"
echo "   2. Find booking: $BOOKING_ID"
echo "   3. Click 'Detail' button"
echo "   4. Check for 'Nominal Refund' field (should show Rp 100,000 in green)"
echo ""
