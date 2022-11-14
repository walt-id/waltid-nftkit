"use strict";
// Copyright (C) 2016 Dmitry Chestnykh
// MIT License. See LICENSE file for details.
Object.defineProperty(exports, "__esModule", { value: true });
const secretbox_1 = require("./secretbox");
const benchmark_1 = require("@stablelib/benchmark");
const buf8192 = (0, benchmark_1.byteSeq)(8192);
const buf1111 = (0, benchmark_1.byteSeq)(1111);
const key = (0, benchmark_1.byteSeq)(32);
const nonce = (0, benchmark_1.byteSeq)(24);
(0, benchmark_1.report)("secretBox 8K", (0, benchmark_1.benchmark)(() => (0, secretbox_1.secretBox)(key, nonce, buf8192), buf8192.length));
(0, benchmark_1.report)("secretBox 1111", (0, benchmark_1.benchmark)(() => (0, secretbox_1.secretBox)(key, nonce, buf1111), buf1111.length));
//# sourceMappingURL=secretbox.bench.js.map