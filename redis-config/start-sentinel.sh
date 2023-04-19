redis-server master.conf &
redis-server slave-1.conf &
redis-server slave-2.conf &

redis-server sentinel-1.conf --sentinel &
redis-server sentinel-2.conf --sentinel &
redis-server sentinel-3.conf --sentinel &