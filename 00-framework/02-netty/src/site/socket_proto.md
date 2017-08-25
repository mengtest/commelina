# 网络层

 * BEFORE ENCODE (300 bytes)       AFTER ENCODE (302 bytes)
 * +---------------+               +--------+---------------+
 * | Protobuf Data |-------------->| Length | Protobuf Data |
 * |  (300 bytes)  |               | 0xAC02 |  (300 bytes)  |
 * +---------------+               +--------+---------------+