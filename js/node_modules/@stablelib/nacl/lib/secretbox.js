"use strict";
// Copyright (C) 2016 Dmitry Chestnykh
// MIT License. See LICENSE file for details.
Object.defineProperty(exports, "__esModule", { value: true });
exports.generateKey = exports.openSecretBox = exports.secretBox = void 0;
const xsalsa20_1 = require("@stablelib/xsalsa20");
const poly1305_1 = require("@stablelib/poly1305");
const wipe_1 = require("@stablelib/wipe");
const random_1 = require("@stablelib/random");
function secretBox(key, nonce, data) {
    if (nonce.length !== 24) {
        throw new Error("secretBox nonce must be 24 bytes");
    }
    const firstBlock = new Uint8Array(64);
    // Allocate place for nonce and counter.
    const nonceCounter = new Uint8Array(24 + 8);
    // Set first bytes to nonce. Last 8 bytes will be counter.
    nonceCounter.set(nonce);
    // Generate first block of XSalsa20 stream, of which
    // first 32 bytes will be authentication key, and the rest
    // will be used for encryption.
    (0, xsalsa20_1.stream)(key, nonceCounter, firstBlock, 8);
    // Allocate result, which will contain 16-byte authenticator
    // concatenated with ciphertext.
    const result = new Uint8Array(16 + data.length);
    // Encrypt first 32 bytes of data with last 32 bytes of generated stream.
    for (let i = 0; i < 32 && i < data.length; i++) {
        result[16 + i] = data[i] ^ firstBlock[32 + i];
    }
    // Encrypt the rest of data.
    if (data.length > 32) {
        (0, xsalsa20_1.streamXOR)(key, nonceCounter, data.subarray(32), result.subarray(16 + 32), 8);
    }
    // Calculate Poly1305 authenticator of encrypted data using
    // authentication key in the first block of XSalsa20 stream.
    const auth = (0, poly1305_1.oneTimeAuth)(firstBlock.subarray(0, 32), result.subarray(16));
    // Copy authenticator to the beginning of result.
    for (let i = 0; i < auth.length; i++) {
        result[i] = auth[i];
    }
    // Clean auth.
    (0, wipe_1.wipe)(auth);
    // Clean first block.
    (0, wipe_1.wipe)(firstBlock);
    // Clean nonceCounter.
    (0, wipe_1.wipe)(nonceCounter);
    return result;
}
exports.secretBox = secretBox;
function openSecretBox(key, nonce, box) {
    if (nonce.length !== 24) {
        throw new Error("secretBox nonce must be 24 bytes");
    }
    if (box.length < 16) {
        throw new Error("secretBox data must be at least 16 bytes");
    }
    const firstBlock = new Uint8Array(64);
    // Allocate place for nonce and counter.
    const nonceCounter = new Uint8Array(24 + 8);
    // Set first bytes to nonce. Last 8 bytes will be counter.
    nonceCounter.set(nonce);
    // Generate first block of XSalsa20 stream, of which
    // first 32 bytes will be authentication key, and the rest
    // will be used for encryption.
    (0, xsalsa20_1.stream)(key, nonceCounter, firstBlock, 8);
    // Calculate Poly1305 authenticator of encrypted data using
    // authentication key in the first block of XSalsa20 stream.
    const auth = (0, poly1305_1.oneTimeAuth)(firstBlock.subarray(0, 32), box.subarray(16));
    // Check authenticator.
    if (!(0, poly1305_1.equal)(auth, box.subarray(0, 16))) {
        // Authenticator is incorrect: ciphertext or authenticator
        // was corrupted, maybe maliciously.
        return null;
    }
    // Authenticator verifies, so we can decrypt ciphertext.
    const ciphertext = box.subarray(16);
    // Allocate result array.
    const result = new Uint8Array(ciphertext.length);
    // Decrypt first 32 bytes of box with last 32 bytes of generated stream.
    for (let i = 0; i < 32 && i < ciphertext.length; i++) {
        result[i] = ciphertext[i] ^ firstBlock[32 + i];
    }
    // Decrypt the rest of data.
    if (ciphertext.length > 32) {
        (0, xsalsa20_1.streamXOR)(key, nonceCounter, ciphertext.subarray(32), result.subarray(32), 8);
    }
    // Clean auth.
    (0, wipe_1.wipe)(auth);
    // Clean first block.
    (0, wipe_1.wipe)(firstBlock);
    // Clean nonceCounter.
    (0, wipe_1.wipe)(nonceCounter);
    return result;
}
exports.openSecretBox = openSecretBox;
/** Generates a 32-byte random secret key.  */
function generateKey(prng) {
    return (0, random_1.randomBytes)(32, prng);
}
exports.generateKey = generateKey;
//# sourceMappingURL=secretbox.js.map