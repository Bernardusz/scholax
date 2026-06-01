# Scholax 🚀

Scholax is a high-performance, open-source Learning Management System (LMS) designed for academic institutions requiring responsive web and native multi-client applications under strict infrastructure resource constraints.

By eliminating heavy ORM caching and resource-intensive JavaScript single-page build systems, Scholax maximizes database I/O efficiency to deliver sub-100ms response times on a microscopic memory footprint.

## 🛠️ The Asymmetric Architecture
- **Web Runtime:** Spring Boot 4.0.6 (Java 25) + Thymeleaf + HTMX.
- **Native Ecosystem:** Jetpack Compose Multiplatform (Unified mobile & desktop apps via Kotlin).
- **Database Access:** Direct SQL execution utilizing Spring's lightweight `JdbcClient`.
- **Zero-Cost Storage:** Client-side data pipeline via Google Picker API directly to user-allocated cloud drives.

---

## 🗺️ 2-Year Engineering Roadmap

Scholax is built using an iterative, technical debt-free migration strategy:

1. **Phase 1 (MVP - Current):** Spring Boot Java + Thymeleaf + JdbcClient + Google Picker API.
2. **Phase 2 (UX Upgrade):** Monolith modernization dropping full-page refreshes via **HTMX**.
3. **Phase 3 (Language Shift):** Full backend migration to **Kotlin** for native asynchronous scalability.
4. **Phase 4 (Enterprise Integration):** Server-side Google OAuth2 token handshake and automated backend file management.
5. **Phase 5 (Multiplatform Future):** Decoupling controllers into secure REST endpoints to power a unified **Jetpack Compose Multiplatform** application (Android, iOS, Windows, macOS).
6. **Final QoL Optimization:** Low-overhead in-memory distributed caching and custom cache invalidation pipelines leveraging **Redis**.

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more details. (Feel free to branch, modify, or deploy for your educational institution—just retain attribution!)