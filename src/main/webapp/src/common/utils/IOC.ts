import {Kernel} from "inversify";
import {makeFluentProvideDecorator} from "inversify-binding-decorators";
import getDecorators from "inversify-inject-decorators";

export const kernel = new Kernel();

const provide = makeFluentProvideDecorator(kernel);

export function provideInstance (identifier: string|Symbol|interfaces.Newable<any>) {
  return provide(identifier).done();
}

export function provideSingleton (identifier: string|Symbol|interfaces.Newable<any>) {
  return provide(identifier).inSingletonScope().done();
}

export const inject = getDecorators(kernel).lazyInject;
