package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.setting.SettingDataManager
import rx.Observable
import rx.subjects.PublishSubject

import javax.inject.Inject

@AppScope
class FooterStateManager @Inject constructor(private val settingDataManager: SettingDataManager) {

  private val subject: PublishSubject<Unit> = PublishSubject.create()

  fun get(): Observable<State> {
    return subject
        .startWith(Unit)
        .switchMap {
          Observable.zip(
              settingDataManager.footerEnabled(),
              settingDataManager.footerText(),
              ::State).first()
        }
  }

  fun set(state: State) {
    settingDataManager.footerEnabled(state.enabled)
    settingDataManager.footerText(state.text.trim())
    subject.onNext(Unit)
  }

  data class State(val enabled: Boolean,
                   val text: String)
}