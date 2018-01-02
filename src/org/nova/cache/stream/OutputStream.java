package org.nova.cache.stream;

import java.math.BigInteger;

import org.nova.cache.utility.CacheConstants;



public final class OutputStream extends Stream {

	private static final int[] BIT_MASK = new int[32];
	private int opcodeStart = 0;

	static {
		for (int i = 0; i < 32; i++)
			BIT_MASK[i] = (1 << i) - 1;
	}
	
	public OutputStream(int capacity) {
		setBuffer(new byte[capacity]);
	}

	public OutputStream() {
		setBuffer(new byte[16]);
	}

	public OutputStream(byte[] buffer) {
		this.setBuffer(buffer);
		this.offset = buffer.length;
		length = buffer.length;
	}

	
	public OutputStream(int[] buffer) {
		setBuffer(new byte[buffer.length]);
		for(int value : buffer)
			writeByte(value);
	}
	
	public void checkCapacityPosition(int position) {
		if (position >= getBuffer().length) {
			byte[] newBuffer = new byte[position + 16];
			System.arraycopy(getBuffer(), 0, newBuffer, 0, getBuffer().length);
			setBuffer(newBuffer);
		}
	}

	public void skip(int length) {
		setOffset(getOffset() + length);
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}


	public void writeBytes(byte[] b, int offset, int length) {
		checkCapacityPosition(this.getOffset() + length - offset);
		System.arraycopy(b, offset, getBuffer(), this.getOffset(), length);
		this.setOffset(this.getOffset() + (length - offset));
	}

	public void writeBytes(byte[] b) {
		int offset = 0;
		int length = b.length;
		checkCapacityPosition(this.getOffset() + length - offset);
		System.arraycopy(b, offset, getBuffer(), this.getOffset(), length);
		this.setOffset(this.getOffset() + (length - offset));
	}

	public void addBytes128(byte[] data, int offset, int len) {
		for (int k = offset; k < len; k++)
			writeByte((byte) (data[k] + 128));
	}

	public void addBytesS(byte[] data, int offset, int len) {
		for (int k = offset; k < len; k++)
			writeByte((byte) (-128 + data[k]));
	}

	public void addBytes_Reverse(byte[] data, int offset, int len) {
		for (int i = len - 1; i >= 0; i--) {
			writeByte((byte) (data[i]));
		}
	}

	public void addBytes_Reverse128(byte[] data, int offset, int len) {
		for (int i = len - 1; i >= 0; i--) {
			writeByte((byte) (data[i] + 128));
		}
	}

	public void writeByte(int i) {
		writeByte(i, offset++);
	}
	
	public void writeNegativeByte(int i) {
		writeByte(-i, offset++);
	}

	public void writeByte(int i, int position) {
		checkCapacityPosition(position);
		getBuffer()[position] = (byte) i;
	}

	public void writeByte128(int i) {
		writeByte(i + 128);
	}

	public void writeByteC(int i) {
		writeByte(-i);
	}

	public void write3Byte(int i) {
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	public void write128Byte(int i) {
		writeByte(128 - i);
	}

	public void writeShortLE128(int i) {
		writeByte(i + 128);
		writeByte(i >> 8);
	}

	public void writeShort128(int i) {
		writeByte(i >> 8);
		writeByte(i + 128);
	}

	@SuppressWarnings("unused")
	public void writeBigSmart(int i) {
		 if(CacheConstants.CLIENT_BUILD < 670) {
			 writeShort(i);
			 return;
		 }
		if(i >= Short.MAX_VALUE && i >= 0)
			writeInt(i-Integer.MAX_VALUE-1);
		else {
			writeShort(i >= 0 ? i : 32767);
		}
	}

	
	public void writeSmart(int i) {
		if (i >= 128) {
			writeShort(i + 32768);
		} else {
			writeByte(i);
		}
	}

	public void writeShort(int i) {
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeShortLE(int i) {
		writeByte(i);
		writeByte(i >> 8);
	}

	public void write24BitInt(int i) {
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	@Override
	public void writeInt(int i) {
		writeByte(i >> 24);
		writeByte(i >> 16);
		writeByte(i >> 8);
		writeByte(i);
	}

	public void writeIntV1(int i) {
		writeByte(i >> 8);
		writeByte(i);
		writeByte(i >> 24);
		writeByte(i >> 16);
	}

	public void writeIntV2(int i) {
		writeByte(i >> 16);
		writeByte(i >> 24);
		writeByte(i);
		writeByte(i >> 8);
	}

	public void writeIntLE(int i) {
		writeByte(i);
		writeByte(i >> 8);
		writeByte(i >> 16);
		writeByte(i >> 24);
	}

	public void writeLong(long l) {
		writeByte((int) (l >> 56));
		writeByte((int) (l >> 48));
		writeByte((int) (l >> 40));
		writeByte((int) (l >> 32));
		writeByte((int) (l >> 24));
		writeByte((int) (l >> 16));
		writeByte((int) (l >> 8));
		writeByte((int) l);
	}

	public void writePSmarts(int i) {
		if (i < 128) {
			writeByte(i);
			return;
		}
		if (i < 32768) {
			writeShort(32768 + i);
			return;
		} else {
			System.out.println("Error psmarts out of range:");
			return;
		}
	}

	public void writeString(String s) {
		checkCapacityPosition(getOffset() + s.length() + 1);
		System.arraycopy(s.getBytes(), 0, getBuffer(), getOffset(), s.length());
		setOffset(getOffset() + s.length());
		writeByte(0);
	}

	public void writeGJString(String s) {
		writeByte(0);
		writeString(s);
	}

	public void putGJString3(String s) {
		writeByte(0);
		writeString(s);
		writeByte(0);
	}
	
	public void writePacket(int id) {
		writeByte(id);
	}
	
	public void writePacketVarByte(int id) {
		writePacket(id);
		writeByte(0);
		opcodeStart = getOffset() - 1;
	}

	public void writePacketVarShort(int id) {
		writePacket(id);
		writeShort(0);
		opcodeStart = getOffset() - 2;
	}

	/*
	 * public void writePacketShort(int id) { writeByte(id); writeShort(0);
	 * opcodeStart = getOffset() - 2; }
	 */

	public void endPacketVarByte() {
		writeByte(getOffset() - (opcodeStart + 2) + 1, opcodeStart);
	}

	public void endPacketVarShort() {
		int size = getOffset() - (opcodeStart + 2);
		writeByte(size >> 8, opcodeStart++);
		writeByte(size, opcodeStart);
	}

	public void initBitAccess() {
		bitPosition = getOffset() * 8;
	}

	public void finishBitAccess() {
		setOffset((bitPosition + 7) / 8);
	}

	public int getBitPos(int i) {
		return 8 * i - bitPosition;
	}

	public void writeBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		for (; numBits > bitOffset; bitOffset = 8) {
			checkCapacityPosition(bytePos);
			getBuffer()[bytePos] &= ~BIT_MASK[bitOffset];
			getBuffer()[bytePos++] |= value >> numBits - bitOffset
					& BIT_MASK[bitOffset];
			numBits -= bitOffset;
		}
		checkCapacityPosition(bytePos);
		if (numBits == bitOffset) {
			getBuffer()[bytePos] &= ~BIT_MASK[bitOffset];
			getBuffer()[bytePos] |= value & BIT_MASK[bitOffset];
		} else {
			getBuffer()[bytePos] &= ~(BIT_MASK[numBits] << bitOffset - numBits);
			getBuffer()[bytePos] |= (value & BIT_MASK[numBits]) << bitOffset
					- numBits;
		}
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	public final void rsaEncode(BigInteger key, BigInteger modulus) {
		int length = offset;
		offset = 0;
		byte data[] = new byte[length];
		setBytes(data, 0, length);
		BigInteger biginteger2 = new BigInteger(data);
		BigInteger biginteger3 = biginteger2.modPow(key, modulus);
		byte out[] = biginteger3.toByteArray();
		offset = 0;
		writeBytes(out, 0, out.length);
	}

	public final void writeJagString(String string) {
		int i_26_ = string.indexOf('\0');
		if (i_26_ >= 0)
			throw new IllegalArgumentException("NULL character at " + i_26_ + " - cannot pjstr2");
		buffer[offset++] = (byte) 0;
		offset += method1801(string, string.length(), buffer, true, offset, 0);
		buffer[offset++] = (byte) 0;
	}
	
	public static final int method1801(String string, int i, byte[] bs, boolean bool, int i_2_, int i_3_) {
		int i_4_ = -i_3_ + i;
		if (bool != true)
			return 5;
		for (int i_5_ = 0; i_5_ < i_4_; i_5_++) {
			int i_6_ = string.charAt(i_3_ + i_5_);
			if ((i_6_ ^ 0xffffffff) < -1 && i_6_ < 128 || (i_6_ ^ 0xffffffff) <= -161 && i_6_ <= 255)
				bs[i_2_ + i_5_] = (byte) i_6_;
			else if ((i_6_ ^ 0xffffffff) != -8365) {
				if ((i_6_ ^ 0xffffffff) != -8219) {
					if (i_6_ == 402)
						bs[i_5_ + i_2_] = (byte) -125;
					else if (i_6_ != 8222) {
						if (i_6_ != 8230) {
							if (i_6_ == 8224)
								bs[i_5_ + i_2_] = (byte) -122;
							else if (i_6_ != 8225) {
								if ((i_6_ ^ 0xffffffff) == -711)
									bs[i_2_ - -i_5_] = (byte) -120;
								else if (i_6_ != 8240) {
									if ((i_6_ ^ 0xffffffff) != -353) {
										if (i_6_ == 8249)
											bs[i_2_ - -i_5_] = (byte) -117;
										else if ((i_6_ ^ 0xffffffff) != -339) {
											if (i_6_ != 381) {
												if (i_6_ == 8216)
													bs[i_5_ + i_2_] = (byte) -111;
												else if ((i_6_ ^ 0xffffffff) != -8218) {
													if (i_6_ != 8220) {
														if ((i_6_ ^ 0xffffffff) != -8222) {
															if (i_6_ == 8226)
																bs[i_5_ + i_2_] = (byte) -107;
															else if ((i_6_ ^ 0xffffffff) == -8212)
																bs[i_2_ + i_5_] = (byte) -106;
															else if (i_6_ != 8212) {
																if ((i_6_ ^ 0xffffffff) == -733)
																	bs[i_2_ - -i_5_] = (byte) -104;
																else if (i_6_ != 8482) {
																	if ((i_6_ ^ 0xffffffff) == -354)
																		bs[i_5_ + i_2_] = (byte) -102;
																	else if ((i_6_ ^ 0xffffffff) == -8251)
																		bs[i_5_ + i_2_] = (byte) -101;
																	else if ((i_6_ ^ 0xffffffff) == -340)
																		bs[i_2_ + i_5_] = (byte) -100;
																	else if (i_6_ != 382) {
																		if (i_6_ != 376)
																			bs[i_2_ + i_5_] = (byte) 63;
																		else
																			bs[i_5_ + i_2_] = (byte) -97;
																	} else
																		bs[i_5_ + i_2_] = (byte) -98;
																} else
																	bs[i_5_ + i_2_] = (byte) -103;
															} else
																bs[i_5_ + i_2_] = (byte) -105;
														} else
															bs[i_2_ - -i_5_] = (byte) -108;
													} else
														bs[i_2_ - -i_5_] = (byte) -109;
												} else
													bs[i_2_ - -i_5_] = (byte) -110;
											} else
												bs[i_2_ + i_5_] = (byte) -114;
										} else
											bs[i_2_ + i_5_] = (byte) -116;
									} else
										bs[i_5_ + i_2_] = (byte) -118;
								} else
									bs[i_5_ + i_2_] = (byte) -119;
							} else
								bs[i_5_ + i_2_] = (byte) -121;
						} else
							bs[i_2_ + i_5_] = (byte) -123;
					} else
						bs[i_2_ - -i_5_] = (byte) -124;
				} else
					bs[i_2_ + i_5_] = (byte) -126;
			} else
				bs[i_5_ + i_2_] = (byte) -128;
		}
		return i_4_;
	}

}