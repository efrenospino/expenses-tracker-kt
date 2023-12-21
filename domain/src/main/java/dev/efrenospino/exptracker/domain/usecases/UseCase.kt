package dev.efrenospino.exptracker.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


interface UseCase<I : UseCase.Args, O> {

    interface Async<I : Args, O> : UseCase<I, O> {
        suspend operator fun invoke(dispatcher: CoroutineDispatcher = Dispatchers.IO, args: I): O
    }

    interface Run<I : Args, O> : UseCase<I, O> {
        operator fun invoke(args: I): O
    }

    interface Args

}

