#!/bin/bash
echo "=== Cek Pods Status ==="
sudo k3s kubectl get pods

echo -e "\n=== Cek Pod Details ==="
sudo k3s kubectl describe pod -l app=accommodation-ti | grep -A 5 "Image:"

echo -e "\n=== Cek Pod Age (untuk lihat kapan pod terakhir dibuat) ==="
sudo k3s kubectl get pods -o wide
