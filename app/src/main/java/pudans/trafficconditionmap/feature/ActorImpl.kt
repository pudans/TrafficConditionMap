package pudans.trafficconditionmap.feature

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pudans.trafficconditionmap.api.ApiService
import pudans.trafficconditionmap.ui.state.ScreenState

class ActorImpl : Actor<ScreenState, Wish, Effect> {

    private val mService by lazy {
        ApiService.create()
    }

    override fun invoke(state: ScreenState, wish: Wish): Observable<Effect> = when (wish) {

        is Wish.UpdateTrafficImages ->
            mService.getTrafficImages(wish.currentTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { Effect.OnLoaded(it) as Effect }
                .startWith(just(Effect.OnStartLoading))
                .onErrorReturn { Effect.ErrorLoading(it) }
    }
}