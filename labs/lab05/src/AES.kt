/**
 * file: AESCipher.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Lab 04
 * due date: April 4th, 2018
 * version: 1.0
 * Converts a 32 bit hexidecimal String
 * to 11 secure 32 bit hexidecimal Strings.
 */

/**
 * Class the houses the functions for AES
 * encryption and decryption.
 */
class AES {

  // Values for SBox substitution:
  private val initialSBox: MutableList<Int> = mutableListOf(
          0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
          0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
          0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
          0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
          0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
          0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
          0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
          0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
          0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
          0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
          0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
          0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
          0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
          0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
          0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
          0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16
  )

  // Values for SBox revert values:
  private val inverseSBox: MutableList<Int> = mutableListOf(
          0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB,
          0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB,
          0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E,
          0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25,
          0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92,
          0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84,
          0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06,
          0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
          0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73,
          0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E,
          0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B,
          0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4,
          0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F,
          0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF,
          0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61,
          0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D
  )

  // Values for round constants:
  private val rCon: MutableList<Int> = mutableListOf(
          0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
          0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
          0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a,
          0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8,
          0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
          0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc,
          0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
          0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3,
          0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94,
          0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20,
          0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35,
          0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f,
          0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04,
          0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63,
          0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd,
          0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d
  )

  // Galois multiplication *2 values
  private val mult2: MutableList<Int> = mutableListOf(
          0x00, 0x02, 0x04, 0x06, 0x08, 0x0a, 0x0c, 0x0e, 0x10, 0x12, 0x14, 0x16, 0x18, 0x1a, 0x1c, 0x1e,
          0x20, 0x22, 0x24, 0x26, 0x28, 0x2a, 0x2c, 0x2e, 0x30, 0x32, 0x34, 0x36, 0x38, 0x3a, 0x3c, 0x3e,
          0x40, 0x42, 0x44, 0x46, 0x48, 0x4a, 0x4c, 0x4e, 0x50, 0x52, 0x54, 0x56, 0x58, 0x5a, 0x5c, 0x5e,
          0x60, 0x62, 0x64, 0x66, 0x68, 0x6a, 0x6c, 0x6e, 0x70, 0x72, 0x74, 0x76, 0x78, 0x7a, 0x7c, 0x7e,
          0x80, 0x82, 0x84, 0x86, 0x88, 0x8a, 0x8c, 0x8e, 0x90, 0x92, 0x94, 0x96, 0x98, 0x9a, 0x9c, 0x9e,
          0xa0, 0xa2, 0xa4, 0xa6, 0xa8, 0xaa, 0xac, 0xae, 0xb0, 0xb2, 0xb4, 0xb6, 0xb8, 0xba, 0xbc, 0xbe,
          0xc0, 0xc2, 0xc4, 0xc6, 0xc8, 0xca, 0xcc, 0xce, 0xd0, 0xd2, 0xd4, 0xd6, 0xd8, 0xda, 0xdc, 0xde,
          0xe0, 0xe2, 0xe4, 0xe6, 0xe8, 0xea, 0xec, 0xee, 0xf0, 0xf2, 0xf4, 0xf6, 0xf8, 0xfa, 0xfc, 0xfe,
          0x1b, 0x19, 0x1f, 0x1d, 0x13, 0x11, 0x17, 0x15, 0x0b, 0x09, 0x0f, 0x0d, 0x03, 0x01, 0x07, 0x05,
          0x3b, 0x39, 0x3f, 0x3d, 0x33, 0x31, 0x37, 0x35, 0x2b, 0x29, 0x2f, 0x2d, 0x23, 0x21, 0x27, 0x25,
          0x5b, 0x59, 0x5f, 0x5d, 0x53, 0x51, 0x57, 0x55, 0x4b, 0x49, 0x4f, 0x4d, 0x43, 0x41, 0x47, 0x45,
          0x7b, 0x79, 0x7f, 0x7d, 0x73, 0x71, 0x77, 0x75, 0x6b, 0x69, 0x6f, 0x6d, 0x63, 0x61, 0x67, 0x65,
          0x9b, 0x99, 0x9f, 0x9d, 0x93, 0x91, 0x97, 0x95, 0x8b, 0x89, 0x8f, 0x8d, 0x83, 0x81, 0x87, 0x85,
          0xbb, 0xb9, 0xbf, 0xbd, 0xb3, 0xb1, 0xb7, 0xb5, 0xab, 0xa9, 0xaf, 0xad, 0xa3, 0xa1, 0xa7, 0xa5,
          0xdb, 0xd9, 0xdf, 0xdd, 0xd3, 0xd1, 0xd7, 0xd5, 0xcb, 0xc9, 0xcf, 0xcd, 0xc3, 0xc1, 0xc7, 0xc5,
          0xfb, 0xf9, 0xff, 0xfd, 0xf3, 0xf1, 0xf7, 0xf5, 0xeb, 0xe9, 0xef, 0xed, 0xe3, 0xe1, 0xe7, 0xe5
  )

  // Galois multiplication *3 values
  private val mult3: MutableList<Int> = mutableListOf(
          0x00, 0x03, 0x06, 0x05, 0x0c, 0x0f, 0x0a, 0x09, 0x18, 0x1b, 0x1e, 0x1d, 0x14, 0x17, 0x12, 0x11,
          0x30, 0x33, 0x36, 0x35, 0x3c, 0x3f, 0x3a, 0x39, 0x28, 0x2b, 0x2e, 0x2d, 0x24, 0x27, 0x22, 0x21,
          0x60, 0x63, 0x66, 0x65, 0x6c, 0x6f, 0x6a, 0x69, 0x78, 0x7b, 0x7e, 0x7d, 0x74, 0x77, 0x72, 0x71,
          0x50, 0x53, 0x56, 0x55, 0x5c, 0x5f, 0x5a, 0x59, 0x48, 0x4b, 0x4e, 0x4d, 0x44, 0x47, 0x42, 0x41,
          0xc0, 0xc3, 0xc6, 0xc5, 0xcc, 0xcf, 0xca, 0xc9, 0xd8, 0xdb, 0xde, 0xdd, 0xd4, 0xd7, 0xd2, 0xd1,
          0xf0, 0xf3, 0xf6, 0xf5, 0xfc, 0xff, 0xfa, 0xf9, 0xe8, 0xeb, 0xee, 0xed, 0xe4, 0xe7, 0xe2, 0xe1,
          0xa0, 0xa3, 0xa6, 0xa5, 0xac, 0xaf, 0xaa, 0xa9, 0xb8, 0xbb, 0xbe, 0xbd, 0xb4, 0xb7, 0xb2, 0xb1,
          0x90, 0x93, 0x96, 0x95, 0x9c, 0x9f, 0x9a, 0x99, 0x88, 0x8b, 0x8e, 0x8d, 0x84, 0x87, 0x82, 0x81,
          0x9b, 0x98, 0x9d, 0x9e, 0x97, 0x94, 0x91, 0x92, 0x83, 0x80, 0x85, 0x86, 0x8f, 0x8c, 0x89, 0x8a,
          0xab, 0xa8, 0xad, 0xae, 0xa7, 0xa4, 0xa1, 0xa2, 0xb3, 0xb0, 0xb5, 0xb6, 0xbf, 0xbc, 0xb9, 0xba,
          0xfb, 0xf8, 0xfd, 0xfe, 0xf7, 0xf4, 0xf1, 0xf2, 0xe3, 0xe0, 0xe5, 0xe6, 0xef, 0xec, 0xe9, 0xea,
          0xcb, 0xc8, 0xcd, 0xce, 0xc7, 0xc4, 0xc1, 0xc2, 0xd3, 0xd0, 0xd5, 0xd6, 0xdf, 0xdc, 0xd9, 0xda,
          0x5b, 0x58, 0x5d, 0x5e, 0x57, 0x54, 0x51, 0x52, 0x43, 0x40, 0x45, 0x46, 0x4f, 0x4c, 0x49, 0x4a,
          0x6b, 0x68, 0x6d, 0x6e, 0x67, 0x64, 0x61, 0x62, 0x73, 0x70, 0x75, 0x76, 0x7f, 0x7c, 0x79, 0x7a,
          0x3b, 0x38, 0x3d, 0x3e, 0x37, 0x34, 0x31, 0x32, 0x23, 0x20, 0x25, 0x26, 0x2f, 0x2c, 0x29, 0x2a,
          0x0b, 0x08, 0x0d, 0x0e, 0x07, 0x04, 0x01, 0x02, 0x13, 0x10, 0x15, 0x16, 0x1f, 0x1c, 0x19, 0x1a
  )

  // Galois multiplication *9 values
  private val mult9: MutableList<Int> = mutableListOf(
          0x00, 0x09, 0x12, 0x1b, 0x24, 0x2d, 0x36, 0x3f, 0x48, 0x41, 0x5a, 0x53, 0x6c, 0x65, 0x7e, 0x77,
          0x90, 0x99, 0x82, 0x8b, 0xb4, 0xbd, 0xa6, 0xaf, 0xd8, 0xd1, 0xca, 0xc3, 0xfc, 0xf5, 0xee, 0xe7,
          0x3b, 0x32, 0x29, 0x20, 0x1f, 0x16, 0x0d, 0x04, 0x73, 0x7a, 0x61, 0x68, 0x57, 0x5e, 0x45, 0x4c,
          0xab, 0xa2, 0xb9, 0xb0, 0x8f, 0x86, 0x9d, 0x94, 0xe3, 0xea, 0xf1, 0xf8, 0xc7, 0xce, 0xd5, 0xdc,
          0x76, 0x7f, 0x64, 0x6d, 0x52, 0x5b, 0x40, 0x49, 0x3e, 0x37, 0x2c, 0x25, 0x1a, 0x13, 0x08, 0x01,
          0xe6, 0xef, 0xf4, 0xfd, 0xc2, 0xcb, 0xd0, 0xd9, 0xae, 0xa7, 0xbc, 0xb5, 0x8a, 0x83, 0x98, 0x91,
          0x4d, 0x44, 0x5f, 0x56, 0x69, 0x60, 0x7b, 0x72, 0x05, 0x0c, 0x17, 0x1e, 0x21, 0x28, 0x33, 0x3a,
          0xdd, 0xd4, 0xcf, 0xc6, 0xf9, 0xf0, 0xeb, 0xe2, 0x95, 0x9c, 0x87, 0x8e, 0xb1, 0xb8, 0xa3, 0xaa,
          0xec, 0xe5, 0xfe, 0xf7, 0xc8, 0xc1, 0xda, 0xd3, 0xa4, 0xad, 0xb6, 0xbf, 0x80, 0x89, 0x92, 0x9b,
          0x7c, 0x75, 0x6e, 0x67, 0x58, 0x51, 0x4a, 0x43, 0x34, 0x3d, 0x26, 0x2f, 0x10, 0x19, 0x02, 0x0b,
          0xd7, 0xde, 0xc5, 0xcc, 0xf3, 0xfa, 0xe1, 0xe8, 0x9f, 0x96, 0x8d, 0x84, 0xbb, 0xb2, 0xa9, 0xa0,
          0x47, 0x4e, 0x55, 0x5c, 0x63, 0x6a, 0x71, 0x78, 0x0f, 0x06, 0x1d, 0x14, 0x2b, 0x22, 0x39, 0x30,
          0x9a, 0x93, 0x88, 0x81, 0xbe, 0xb7, 0xac, 0xa5, 0xd2, 0xdb, 0xc0, 0xc9, 0xf6, 0xff, 0xe4, 0xed,
          0x0a, 0x03, 0x18, 0x11, 0x2e, 0x27, 0x3c, 0x35, 0x42, 0x4b, 0x50, 0x59, 0x66, 0x6f, 0x74, 0x7d,
          0xa1, 0xa8, 0xb3, 0xba, 0x85, 0x8c, 0x97, 0x9e, 0xe9, 0xe0, 0xfb, 0xf2, 0xcd, 0xc4, 0xdf, 0xd6,
          0x31, 0x38, 0x23, 0x2a, 0x15, 0x1c, 0x07, 0x0e, 0x79, 0x70, 0x6b, 0x62, 0x5d, 0x54, 0x4f, 0x46
  )

  // // Galois multiplication *11 values
  private val mult11: MutableList<Int> = mutableListOf(
          0x00, 0x0b, 0x16, 0x1d, 0x2c, 0x27, 0x3a, 0x31, 0x58, 0x53, 0x4e, 0x45, 0x74, 0x7f, 0x62, 0x69,
          0xb0, 0xbb, 0xa6, 0xad, 0x9c, 0x97, 0x8a, 0x81, 0xe8, 0xe3, 0xfe, 0xf5, 0xc4, 0xcf, 0xd2, 0xd9,
          0x7b, 0x70, 0x6d, 0x66, 0x57, 0x5c, 0x41, 0x4a, 0x23, 0x28, 0x35, 0x3e, 0x0f, 0x04, 0x19, 0x12,
          0xcb, 0xc0, 0xdd, 0xd6, 0xe7, 0xec, 0xf1, 0xfa, 0x93, 0x98, 0x85, 0x8e, 0xbf, 0xb4, 0xa9, 0xa2,
          0xf6, 0xfd, 0xe0, 0xeb, 0xda, 0xd1, 0xcc, 0xc7, 0xae, 0xa5, 0xb8, 0xb3, 0x82, 0x89, 0x94, 0x9f,
          0x46, 0x4d, 0x50, 0x5b, 0x6a, 0x61, 0x7c, 0x77, 0x1e, 0x15, 0x08, 0x03, 0x32, 0x39, 0x24, 0x2f,
          0x8d, 0x86, 0x9b, 0x90, 0xa1, 0xaa, 0xb7, 0xbc, 0xd5, 0xde, 0xc3, 0xc8, 0xf9, 0xf2, 0xef, 0xe4,
          0x3d, 0x36, 0x2b, 0x20, 0x11, 0x1a, 0x07, 0x0c, 0x65, 0x6e, 0x73, 0x78, 0x49, 0x42, 0x5f, 0x54,
          0xf7, 0xfc, 0xe1, 0xea, 0xdb, 0xd0, 0xcd, 0xc6, 0xaf, 0xa4, 0xb9, 0xb2, 0x83, 0x88, 0x95, 0x9e,
          0x47, 0x4c, 0x51, 0x5a, 0x6b, 0x60, 0x7d, 0x76, 0x1f, 0x14, 0x09, 0x02, 0x33, 0x38, 0x25, 0x2e,
          0x8c, 0x87, 0x9a, 0x91, 0xa0, 0xab, 0xb6, 0xbd, 0xd4, 0xdf, 0xc2, 0xc9, 0xf8, 0xf3, 0xee, 0xe5,
          0x3c, 0x37, 0x2a, 0x21, 0x10, 0x1b, 0x06, 0x0d, 0x64, 0x6f, 0x72, 0x79, 0x48, 0x43, 0x5e, 0x55,
          0x01, 0x0a, 0x17, 0x1c, 0x2d, 0x26, 0x3b, 0x30, 0x59, 0x52, 0x4f, 0x44, 0x75, 0x7e, 0x63, 0x68,
          0xb1, 0xba, 0xa7, 0xac, 0x9d, 0x96, 0x8b, 0x80, 0xe9, 0xe2, 0xff, 0xf4, 0xc5, 0xce, 0xd3, 0xd8,
          0x7a, 0x71, 0x6c, 0x67, 0x56, 0x5d, 0x40, 0x4b, 0x22, 0x29, 0x34, 0x3f, 0x0e, 0x05, 0x18, 0x13,
          0xca, 0xc1, 0xdc, 0xd7, 0xe6, 0xed, 0xf0, 0xfb, 0x92, 0x99, 0x84, 0x8f, 0xbe, 0xb5, 0xa8, 0xa3
  )

  // Galois multiplication *13 values
  private val mult13: MutableList<Int> = mutableListOf(
          0x00, 0x0d, 0x1a, 0x17, 0x34, 0x39, 0x2e, 0x23, 0x68, 0x65, 0x72, 0x7f, 0x5c, 0x51, 0x46, 0x4b,
          0xd0, 0xdd, 0xca, 0xc7, 0xe4, 0xe9, 0xfe, 0xf3, 0xb8, 0xb5, 0xa2, 0xaf, 0x8c, 0x81, 0x96, 0x9b,
          0xbb, 0xb6, 0xa1, 0xac, 0x8f, 0x82, 0x95, 0x98, 0xd3, 0xde, 0xc9, 0xc4, 0xe7, 0xea, 0xfd, 0xf0,
          0x6b, 0x66, 0x71, 0x7c, 0x5f, 0x52, 0x45, 0x48, 0x03, 0x0e, 0x19, 0x14, 0x37, 0x3a, 0x2d, 0x20,
          0x6d, 0x60, 0x77, 0x7a, 0x59, 0x54, 0x43, 0x4e, 0x05, 0x08, 0x1f, 0x12, 0x31, 0x3c, 0x2b, 0x26,
          0xbd, 0xb0, 0xa7, 0xaa, 0x89, 0x84, 0x93, 0x9e, 0xd5, 0xd8, 0xcf, 0xc2, 0xe1, 0xec, 0xfb, 0xf6,
          0xd6, 0xdb, 0xcc, 0xc1, 0xe2, 0xef, 0xf8, 0xf5, 0xbe, 0xb3, 0xa4, 0xa9, 0x8a, 0x87, 0x90, 0x9d,
          0x06, 0x0b, 0x1c, 0x11, 0x32, 0x3f, 0x28, 0x25, 0x6e, 0x63, 0x74, 0x79, 0x5a, 0x57, 0x40, 0x4d,
          0xda, 0xd7, 0xc0, 0xcd, 0xee, 0xe3, 0xf4, 0xf9, 0xb2, 0xbf, 0xa8, 0xa5, 0x86, 0x8b, 0x9c, 0x91,
          0x0a, 0x07, 0x10, 0x1d, 0x3e, 0x33, 0x24, 0x29, 0x62, 0x6f, 0x78, 0x75, 0x56, 0x5b, 0x4c, 0x41,
          0x61, 0x6c, 0x7b, 0x76, 0x55, 0x58, 0x4f, 0x42, 0x09, 0x04, 0x13, 0x1e, 0x3d, 0x30, 0x27, 0x2a,
          0xb1, 0xbc, 0xab, 0xa6, 0x85, 0x88, 0x9f, 0x92, 0xd9, 0xd4, 0xc3, 0xce, 0xed, 0xe0, 0xf7, 0xfa,
          0xb7, 0xba, 0xad, 0xa0, 0x83, 0x8e, 0x99, 0x94, 0xdf, 0xd2, 0xc5, 0xc8, 0xeb, 0xe6, 0xf1, 0xfc,
          0x67, 0x6a, 0x7d, 0x70, 0x53, 0x5e, 0x49, 0x44, 0x0f, 0x02, 0x15, 0x18, 0x3b, 0x36, 0x21, 0x2c,
          0x0c, 0x01, 0x16, 0x1b, 0x38, 0x35, 0x22, 0x2f, 0x64, 0x69, 0x7e, 0x73, 0x50, 0x5d, 0x4a, 0x47,
          0xdc, 0xd1, 0xc6, 0xcb, 0xe8, 0xe5, 0xf2, 0xff, 0xb4, 0xb9, 0xae, 0xa3, 0x80, 0x8d, 0x9a, 0x97
  )

  // Galois multiplication *14 values
  private val mult14: MutableList<Int> = mutableListOf(
          0x00, 0x0e, 0x1c, 0x12, 0x38, 0x36, 0x24, 0x2a, 0x70, 0x7e, 0x6c, 0x62, 0x48, 0x46, 0x54, 0x5a,
          0xe0, 0xee, 0xfc, 0xf2, 0xd8, 0xd6, 0xc4, 0xca, 0x90, 0x9e, 0x8c, 0x82, 0xa8, 0xa6, 0xb4, 0xba,
          0xdb, 0xd5, 0xc7, 0xc9, 0xe3, 0xed, 0xff, 0xf1, 0xab, 0xa5, 0xb7, 0xb9, 0x93, 0x9d, 0x8f, 0x81,
          0x3b, 0x35, 0x27, 0x29, 0x03, 0x0d, 0x1f, 0x11, 0x4b, 0x45, 0x57, 0x59, 0x73, 0x7d, 0x6f, 0x61,
          0xad, 0xa3, 0xb1, 0xbf, 0x95, 0x9b, 0x89, 0x87, 0xdd, 0xd3, 0xc1, 0xcf, 0xe5, 0xeb, 0xf9, 0xf7,
          0x4d, 0x43, 0x51, 0x5f, 0x75, 0x7b, 0x69, 0x67, 0x3d, 0x33, 0x21, 0x2f, 0x05, 0x0b, 0x19, 0x17,
          0x76, 0x78, 0x6a, 0x64, 0x4e, 0x40, 0x52, 0x5c, 0x06, 0x08, 0x1a, 0x14, 0x3e, 0x30, 0x22, 0x2c,
          0x96, 0x98, 0x8a, 0x84, 0xae, 0xa0, 0xb2, 0xbc, 0xe6, 0xe8, 0xfa, 0xf4, 0xde, 0xd0, 0xc2, 0xcc,
          0x41, 0x4f, 0x5d, 0x53, 0x79, 0x77, 0x65, 0x6b, 0x31, 0x3f, 0x2d, 0x23, 0x09, 0x07, 0x15, 0x1b,
          0xa1, 0xaf, 0xbd, 0xb3, 0x99, 0x97, 0x85, 0x8b, 0xd1, 0xdf, 0xcd, 0xc3, 0xe9, 0xe7, 0xf5, 0xfb,
          0x9a, 0x94, 0x86, 0x88, 0xa2, 0xac, 0xbe, 0xb0, 0xea, 0xe4, 0xf6, 0xf8, 0xd2, 0xdc, 0xce, 0xc0,
          0x7a, 0x74, 0x66, 0x68, 0x42, 0x4c, 0x5e, 0x50, 0x0a, 0x04, 0x16, 0x18, 0x32, 0x3c, 0x2e, 0x20,
          0xec, 0xe2, 0xf0, 0xfe, 0xd4, 0xda, 0xc8, 0xc6, 0x9c, 0x92, 0x80, 0x8e, 0xa4, 0xaa, 0xb8, 0xb6,
          0x0c, 0x02, 0x10, 0x1e, 0x34, 0x3a, 0x28, 0x26, 0x7c, 0x72, 0x60, 0x6e, 0x44, 0x4a, 0x58, 0x56,
          0x37, 0x39, 0x2b, 0x25, 0x0f, 0x01, 0x13, 0x1d, 0x47, 0x49, 0x5b, 0x55, 0x7f, 0x71, 0x63, 0x6d,
          0xd7, 0xd9, 0xcb, 0xc5, 0xef, 0xe1, 0xf3, 0xfd, 0xa7, 0xa9, 0xbb, 0xb5, 0x9f, 0x91, 0x83, 0x8d
  )

  /**
   * Takes an input string and returns it as
   * concatenated encrypted ciphertext of
   * length multiples of 32 bits.
   *
   * @param plainText the input string.
   * @param keyHex the key.
   * @return the ciphertext.
   */
  public fun encrypt(plainText: String, keyHex: String): String {

    var string = plainText
    val list = mutableListOf<String>()
    val char = ' '

    // Parse substrings of 16 to a list:
    while (string.isNotBlank()) {
      when (string.length > 15) {
        true -> {
          list.add(string.substring(0, 16).toUpperCase())
          string = string.substring(16)
        }

        false -> {
          val sb = StringBuilder(string)
          while (sb.length != 16)
            sb.append(char)

          string = sb.toString()
        }
      }
    }

    // Parse strings to hex strings:
    val hexStrings = mutableListOf<String>()
    for (sub in list) {
      val sb = StringBuilder()
      for (index in sub.toMutableList()) {
        val byte = Integer.toHexString(index.toInt())
        if (byte.length < 2)
          sb.append("0$byte")
        else
          sb.append(byte)
      }
      hexStrings.add(sb.toString())
    }

    val sb = StringBuilder()
    for (hexString in hexStrings)
      sb.append(encryptHex(hexString, keyHex))

    return sb.toString()
  }

  /**
   * Takes a ciphertext string and decrypts the
   * the cipher back to plaintext.
   *
   * @param cipherText the ciphertext.
   * @param keyHex the key.
   * @return the plaintext.
   */
  public fun decrypt(cipherText: String, keyHex: String): String {

    var string = cipherText
    val list = mutableListOf<String>()

    while (string.isNotBlank()) {
      list.add(string.substring(0, 32))
      string = string.substring(32)
    }

    val sb = StringBuilder()
    for (cipher in list)
      sb.append(decryptHex(cipher, keyHex))

    return hexToChars(sb.toString())
  }

  /**
   * Combines the functions in proper order
   * to encrypt plainHex using a given key.
   *
   * @param pTextHex the plain text in hex format
   * @param keyHex the key.
   * @return the ciphertext.
   */
  private fun encryptHex(pTextHex: String, keyHex: String): String {

    val secureKeys = aesRoundKeys(keyHex)
    val pTextBloc = generateMatrix(pTextHex)
    var keyHexBloc = generateMatrix(secureKeys[0])
    var outStateHex = aesStateXOR(pTextBloc, keyHexBloc)

    for (key in 1..9) {
      keyHexBloc = generateMatrix(secureKeys[key])
      outStateHex = aesNibbleSub(outStateHex)
      outStateHex = aesShiftRow(outStateHex)
      outStateHex = aesMixColumns(outStateHex)
      outStateHex = aesStateXOR(outStateHex, keyHexBloc)
    }

    keyHexBloc = generateMatrix(secureKeys[10])
    outStateHex = aesNibbleSub(outStateHex)
    outStateHex = aesShiftRow(outStateHex)
    outStateHex = aesStateXOR(outStateHex, keyHexBloc)
    return generateCipherText(outStateHex).toUpperCase()
  }

  /**
   * Combines the proper inverse functions
   * in proper order to decrypt ciphertext.
   *
   * @param cTextHex the cipher hex
   * @param keyHex the key.
   * @return the plain hex.
   */
  private fun decryptHex(cTextHex: String, keyHex: String): String {

    val secureKeys = aesRoundKeys(keyHex)
    val cTextBloc = generateMatrix(cTextHex)
    var keyHexBloc = generateMatrix(secureKeys[10])
    var outStateHex = aesStateXOR(cTextBloc, keyHexBloc)
    outStateHex = aesInvShiftRow(outStateHex)
    outStateHex = aesInvNibbleSub(outStateHex)

    for (key in 9 downTo 1) {
      keyHexBloc = generateMatrix(secureKeys[key])
      outStateHex = aesStateXOR(outStateHex, keyHexBloc)
      outStateHex = aesInvMixColumns(outStateHex)
      outStateHex = aesInvShiftRow(outStateHex)
      outStateHex = aesInvNibbleSub(outStateHex)
    }

    keyHexBloc = generateMatrix(secureKeys[0])
    outStateHex = aesStateXOR(outStateHex, keyHexBloc)
    return generateCipherText(outStateHex).toUpperCase()
  }

  /**
   * This method takes 32 bit hex key and generates
   * eleven secure 32 bit hex keys.
   *
   * @param keyHex the 32 bit hex key.
   * @return the String array containing the keys.
   */
  private fun aesRoundKeys(keyHex: String): MutableList<String> {

    // Insure key is of length 32:
    if (keyHex.length != 32)
      return mutableListOf("Incorrect String length")

    // Generate matrix from input key:
    val w = generateMatrix(keyHex)

    // Iterate through and generate each new column:
    for (j in 4..43) {
      var temp: MutableList<String> = mutableListOf()

      // If the column is a multiple of 4, assign j to XOR of columns j - 1 and j - 4
      if (j % 4 != 0)
        for (index in 0..3)
          temp.add(exclusiveOr(w[j - 4][index], w[j - 1][index]))

      // Otherwise, start a new round:
      else {

        // AES RCon Hex String:
        val rCon = aesRCon(j / 4)

        // Construct new list with elements of previous column:
        for (index in 0..3)
          temp.add(w[j - 1][index])

        // Perform a shift on the new array:
        temp = shift(temp)

        // Transform each byte using the SBox:
        for (index in 0..3)
          temp[index] = aesSBoxTransform(Integer.parseInt(temp[index], 16))

        // Perform XOR against the RCon and the first element on the top of the column:
        temp[0] = exclusiveOr(rCon, temp[0])

        // Set column to the contents of the new temp array XOR with column j - 4
        for (index in 0..3)
          temp[index] = exclusiveOr(w[j - 4][index], temp[index])
      }

      // Add new column to the matrix:
      w.add(temp)
    }

    // Convert the 11 4x4 matrices in the 44x4 matrix into 11 secure keys:
    val roundKeys = mutableListOf<String>()
    for (key in 0..10) {
      val sb = StringBuilder()
      while (sb.length != 32) {
        val list = w.removeAt(0)
        for (hex in list)
          sb.append(hex)
      }
      roundKeys.add(sb.toString().toUpperCase())
    }

    return roundKeys
  }

  /**
   * Converts a list of hex characters to a list
   * hex pairs.
   *
   * @param chars the list of chars
   * @return the list containing the pairs of hex chars.
   */
  private fun charsToHexPairs(chars: MutableList<Char>): MutableList<String> {

    val array = mutableListOf<String>()
    while (!chars.isEmpty()) {
      val sb = StringBuilder()
      sb.append(chars.removeAt(0))
      sb.append(chars.removeAt(0))
      array.add(sb.toString())
    }
    return array
  }

  private fun hexToChars(hex: String): String {
    val list = mutableListOf<String>()
    val chars = hex.toMutableList()
    while (chars.isNotEmpty())
      list.add("${chars.removeAt(0)}${chars.removeAt(0)}")
    val sb = StringBuilder()
    for (nibble in list)
      sb.append(Integer.parseInt(nibble, 16).toChar())
    return sb.toString()
  }

  /**
   * Method that simplifies the XOR process and
   * returns the formatted hex byte string.
   *
   * @param first the first hex byte.
   * @param second the second hex byte.
   * @return the formatted post XOR hex byte string.
   */
  private fun exclusiveOr(first: String, second: String): String {

    val sb = StringBuilder("0")
    val hex = Integer.toHexString(Integer.parseInt(first, 16)
            xor Integer.parseInt(second, 16))
    when (hex.length) {
      0 -> return sb.append("0").toString()
      1 -> return sb.append(hex).toString()
    }
    return hex
  }

  /**
   * Simplified method that returns the correct RCon
   * for the specified round in AES.
   *
   * @param round the current round in AES.
   * @return the RCon hex byte.
   */
  private fun aesRCon(round: Int): String =
          Integer.toHexString(rCon[round])

  /**
   * Simplified method that returns the correct SBox
   * value for the specified hex byte in AES.
   *
   * @param inHex the hex byte to be transformed.
   * @return the SBox hex byte.
   */
  private fun aesSBoxTransform(inHex: Int) =
          Integer.toHexString(initialSBox[inHex])

  /**
   * Simplifed method that returns the correct SBox
   * value for the specified hex byte in AES.
   *
   * @param inHex the hex byte to be transformed.
   * @return the SBox hex byte.
   */
  private fun aesSBoxInverse(inHex: Int) =
          Integer.toHexString(inverseSBox[inHex])

  /**
   * Method that simplifies the shift done in AES rounds.
   *
   * @param strings the list of strings.
   * @return the list of strings shifted over by 1.
   */
  private fun shift(strings: MutableList<String>) =
          mutableListOf(strings[1], strings[2], strings[3], strings[0])

  /**
   * Uses the XOR function to manipulate a chosen hex matrix
   * against a secure key.
   *
   * @param inHex the chosen hex string.
   * @param keyHex the hexidecimal key.
   * @return the modified hexidecimal matrix.
   */
  private fun aesStateXOR(inHex: MutableList<MutableList<String>>, keyHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
    for (i in 0..3)
      for (j in 0..3)
        inHex[i][j] = exclusiveOr(inHex[i][j], keyHex[i][j])

    return inHex
  }

  /**
   * Substitutes each hex byte in the given matrix with
   * its corresponding SBox value.
   *
   * @param inStateHex the given matrix.
   * @the modified matrix.
   */
  private fun aesNibbleSub(inStateHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
    for (i in 0..3)
      for (j in 0..3)
        inStateHex[i][j] = aesSBoxTransform(Integer.parseInt(inStateHex[i][j], 16))
    return inStateHex
  }

  /**
   * Subtitutes each hex byte in the given matrix with
   * its corresponding SBox inverse value.
   *
   * @param inStateHex the given matrix.
   * @return the modified matrix.
   */
  private fun aesInvNibbleSub(inStateHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
    for (i in 0..3)
      for (j in 0..3)
        inStateHex[i][j] = aesSBoxInverse(Integer.parseInt(inStateHex[i][j], 16))
    return inStateHex
  }

  /**
   * Performs the necessary shifts on a matrix
   * for AES encryption.
   *
   * @param inStateHex the matrix.
   * @return the modified matrix.
   */
  private fun aesShiftRow(inStateHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
    val temp = mutableListOf<MutableList<String>>()
    for (index in 0..3) {
      val column = mutableListOf(
              inStateHex[index][0],
              inStateHex[(index + 1) % 4][1],
              inStateHex[(index + 2) % 4][2],
              inStateHex[(index + 3) % 4][3])
      temp.add(column)
    }
    return temp
  }

  /**
   * Performs the necessary inverted shifts
   * on a matrix for AES decryption.
   *
   * @param inStateHex the matrix.
   * @return the modified matrix.
   */
  private fun aesInvShiftRow(inStateHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {

    val temp = mutableListOf<MutableList<String>>()
    for (index in 0..3) {
      val column = mutableListOf(
              inStateHex[index][0],
              inStateHex[(index + 3) % 4][1],
              inStateHex[(index + 2) % 4][2],
              inStateHex[(index + 1) % 4][3])
      temp.add(column)
    }
    return temp
  }

  /**
   * Mixes up the given matrix using Rijndael's
   * method of mixing columns by using Galois
   * multiplication.
   *
   * @param inStateHex the given matrix.
   * @return a mixed matrix.
   */
  private fun aesMixColumns(inStateHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {

    // Create new matrix comprised of given matrix in integer format:
    val n = mutableListOf<MutableList<Int>>()
    val ints = mutableListOf<Int>()
    for (i in 0..3)
      for (j in 0..3)
        ints.add(Integer.parseInt(inStateHex[i][j], 16))

    // Perform the XOR operations on each of the columns:
    for (c in 0..3) {
      val temp = mutableListOf<Int>()
      temp.add(ints.removeAt(0))
      temp.add(ints.removeAt(0))
      temp.add(ints.removeAt(0))
      temp.add(ints.removeAt(0))
      n.add(temp)
      inStateHex[c] = mutableListOf(
              Integer.toHexString(mult2[n[c][0]] xor mult3[n[c][1]] xor n[c][2] xor n[c][3]),
              Integer.toHexString(n[c][0] xor mult2[n[c][1]] xor mult3[n[c][2]] xor n[c][3]),
              Integer.toHexString(n[c][0] xor n[c][1] xor mult2[n[c][2]] xor mult3[n[c][3]]),
              Integer.toHexString(mult3[n[c][0]] xor n[c][1] xor n[c][2] xor mult2[n[c][3]]))

      // Format each hex byte:
      for (j in 0..3) {
        val len = inStateHex[c][j].length
        if (len < 2)
          inStateHex[c][j] = "0${inStateHex[c][j]}"
        if (len > 2)
          inStateHex[c][j] = inStateHex[c][j].substring(len - 2, len - 1)
      }
    }
    return inStateHex
  }

  /**
   * Unmixes the given matrix using Rijndael's
   * method of unmixing columns by using Galois
   * multiplication.
   *
   * @param inStateHex the given matrix.
   * @return the unmixed matrix.
   */
  private fun aesInvMixColumns(inStateHex: MutableList<MutableList<String>>): MutableList<MutableList<String>> {

    // Create new matrix comprised of given matrix in integer format:
    val n = mutableListOf<MutableList<Int>>()
    val ints = mutableListOf<Int>()
    for (i in 0..3)
      for (j in 0..3)
        ints.add(Integer.parseInt(inStateHex[i][j], 16))

    for (c in 0..3) {
      val temp = mutableListOf<Int>()
      temp.add(ints.removeAt(0))
      temp.add(ints.removeAt(0))
      temp.add(ints.removeAt(0))
      temp.add(ints.removeAt(0))
      n.add(temp)
      inStateHex[c] = mutableListOf(
              Integer.toHexString(mult14[n[c][0]] xor mult11[n[c][1]] xor mult13[n[c][2]] xor mult9[n[c][3]]),
              Integer.toHexString(mult9[n[c][0]] xor mult14[n[c][1]] xor mult11[n[c][2]] xor mult13[n[c][3]]),
              Integer.toHexString(mult13[n[c][0]] xor mult9[n[c][1]] xor mult14[n[c][2]] xor mult11[n[c][3]]),
              Integer.toHexString(mult11[n[c][0]] xor mult13[n[c][1]] xor mult9[n[c][2]] xor mult14[n[c][3]]))

      // Format each hex byte:
      for (j in 0..3) {
        val len = inStateHex[c][j].length
        if (len < 2)
          inStateHex[c][j] = "0${inStateHex[c][j]}"
        if (len > 2)
          inStateHex[c][j] = inStateHex[c][j].substring(len - 2, len - 1)
      }
    }

    return inStateHex
  }

  /**
   * Takes a 4x4 matrix and converts back to a 32 bit
   * hexidecimal string.
   *
   * @param matrix the 4x4 matrix.
   * @return the 32 bit hex string.
   */
  private fun generateCipherText(matrix: MutableList<MutableList<String>>): String {
    val sb = StringBuilder()
    for (i in 0..3)
      for (j in 0..3)
        sb.append(matrix[i][j])
    return sb.toString()
  }

  /**
   * Converts a hex string to a matrix.
   *
   * @param hexString the hex string to be converted.
   * @return the matrix.
   */
  private fun generateMatrix(hexString: String): MutableList<MutableList<String>> {

    // Group String into list of hex bytes:
    val hexValues = charsToHexPairs(hexString.toMutableList())

    // Create matrix:
    val matrix: MutableList<MutableList<String>> = mutableListOf()

    // Populate matrix with hex bytes:
    while (!hexValues.isEmpty()) {
      val column: MutableList<String> = mutableListOf()
      for (index in 0..3)
        column.add(hexValues.removeAt(0))
      matrix.add(column)
    }
    return matrix
  }
}