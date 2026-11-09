# VK Education: практическое задание 6

Каталог Android-приложений разделён на слои `data`, `domain` и `presentation` по принципам Clean Architecture. Решение продолжает задание 5 с ViewModel и одноразовыми Snackbar.

## Требования задания

- Выделить слои Data, Domain и Presentation.
- Разместить в Presentation интерфейс и ViewModel.
- Перенести исходные данные в Data.
- Возвращать данные через Repository.
- Хранить исходные записи в DTO и преобразовывать их через Mapper в доменную модель.
- Создать ветку решения и Pull Request в основную ветку репозитория.

## Структура

```text
com.example.vkeducation/
├── MainActivity.kt                         # создание зависимостей приложения
├── domain/
│   ├── model/AppInfo.kt                    # AppInfo и AppIcon, чистый Kotlin
│   └── repository/AppsRepository.kt        # контракт получения каталога
├── data/
│   ├── AppDto.kt                           # формат исходной записи
│   ├── HardcodedAppsDataSource.kt          # восемь локальных DTO
│   ├── AppMapper.kt                        # DTO → AppInfo
│   └── LocalAppsRepository.kt              # реализация доменного контракта
└── presentation/
    ├── AppListViewModel.kt                 # StateFlow, события, фабрика
    ├── AppListRoute.kt                     # подписки с учётом Lifecycle
    └── ui/
        ├── AppListScreen.kt                # Compose-экран
        ├── AppIconResources.kt             # AppIcon → R.drawable
        ├── components/AppItem.kt           # карточка приложения
        └── theme/                          # тема Compose
```

Во время загрузки `LocalAppsRepository` получает DTO из `HardcodedAppsDataSource`, преобразует каждую запись через `AppMapper` и возвращает `List<AppInfo>`. ViewModel публикует этот список в состоянии экрана.

Presentation и Data зависят от контракта Domain. Domain содержит обычные Kotlin-типы: Android-ресурсы выбираются в Presentation через `AppIconResources`. Неизвестная иконка из DTO получает значение `GENERIC`, а текстовые поля сохраняются.

`MainActivity` связывает `LocalAppsRepository` с `AppListViewModel.factory(...)`. ViewModel получает Repository через конструктор. Compose-экран получает готовую ViewModel, связанную с Activity.

## Поведение приложения

Сохранены восемь приложений исходного каталога, порядок, тексты и изображения. Нажатие на логотип показывает Snackbar с названием приложения. ViewModel загружает каталог один раз за свой срок жизни. При пересоздании Activity экземпляр ViewModel и состояние сохраняются, а обработанный Snackbar не повторяется.

Источник данных локальный, доступ в интернет не нужен. После завершения процесса Android новый экземпляр ViewModel снова загружает локальный каталог.

## Сборка и проверки

Требуются Android SDK Platform 37, Android 8.0 / API 26 или новее и JDK 25 для Gradle 9.6.0, указанные в исходной конфигурации проекта. Путь Android SDK задаётся через `ANDROID_HOME` или локальный `local.properties`.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest --console=plain
.\gradlew.bat :app:connectedDebugAndroidTest --console=plain
```

Для второй команды нужен запущенный эмулятор или подключённое устройство. На macOS/Linux используйте `./gradlew`.

Проверки этой версии:

- Debug APK и APK инструментальных тестов собираются успешно.
- 10 unit-тестов проходят: преобразование DTO, неизвестные иконки, инъекция источника и Mapper, порядок данных, исходный каталог и поведение ViewModel.
- Android Lint проходит без ошибок; отчёт содержит 16 предупреждений.
- Инструментальные тесты проходят на эмуляторе Android 17 / API 37, включая пересоздание Activity и отсутствие повторного Snackbar.

Ручная проверка: запустите каталог, нажмите на логотип Telegram, поверните устройство во время показа сообщения, затем нажмите на логотип ещё раз. Список сохраняется, старое сообщение закрывается, новое нажатие показывает новый Snackbar.

## Файлы результата

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Unit-отчёт: `app/build/reports/tests/testDebugUnitTest/index.html`.
- Отчёт UI-тестов: `app/build/reports/androidTests/connected/debug/index.html`.
- Lint: `app/build/reports/lint-results-debug.html`.

Ветка: `homework/06-clean-architecture`, основана на `homework/05-viewmodel-state`. Для сдачи нужны ссылка на эту ветку репозитория и ссылка на Pull Request в `main`.
