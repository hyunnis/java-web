console.log("===== 1. 스코프 차이 =====");
if (true) {
    var a = "var 변수";
    let b = "let 변수";
    const c = "const 변수";
}

console.log("var a", a) //접근가능
// console.log("let b", b) //ref 에러
// console.log("const c", c) //ref 에러

console.log("===== 2.재선언&재할당 =====");

var x = 10;
var x = 20; // 가능
console.log("var 재선언:", x); // 재선언 + 재할당 OK
// -> 기존 변수를 덮어씀

let y = 30;
// let y = 40; // X 에러 (재선언 불가)
y = 40; //재할당 가능
console.log("let 재할당:", y); // 재선언 불가 X
// -> 값만 변경

const z = 50;
// z = 60; // X 에러 (재할당 불가)
console.log("const 값:", z);
// -> 값 변경 자체 불가

console.log("===== 3. 호이스팅 =====");

console.log(testVar); // undefined
var testVar = 100;

console.log(testLet); // X referenceError
let testLet = 200;

console.log(testConst); // X referenceError
const testConst = 300;