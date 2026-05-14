<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp" />

        <main>
            <!-- Hero Banner -->
            <section class="container-fluid py-4">
                <div class="main-hero shadow-xl mx-auto">
                    <div class="px-4 text-white">
                        <c:if test="${isAdmin}">
                            <span class="badge rounded-pill bg-primary bg-opacity-25 px-4 py-2 mb-4 fw-bold shadow-sm hero-badge">Premium Forest Stay (Admin Mode)</span>
                        </c:if>
                        <c:if test="${not isAdmin}">
                            <span class="badge rounded-pill bg-primary bg-opacity-25 px-4 py-2 mb-4 fw-bold shadow-sm hero-badge">Premium Forest Stay</span>
                        </c:if>
                        <c:if test="${isAdmin}">
                            <h2 class="display-3 fw-black mb-4 lh-tight">관리자님, 환영합니다.<br>시스템 현황을 확인하고 관리하세요.</h2>
                        </c:if>
                        <c:if test="${not isAdmin}">
                            <h2 class="display-3 fw-black mb-4 lh-tight">도시의 소음을 벗어나 숲의 품으로,<br>포레스트 헤이븐</h2>
                        </c:if>
                        <p class="fs-5 fw-medium mb-5 opacity-90">자연과 하나되는 특별한 휴식, 지금 시작하세요.</p>
                        <a href="/reservations/new" class="btn-reserve">
                            실시간 예약하기
                            <span class="material-symbols-outlined align-middle">arrow_forward</span>
                        </a>
                    </div>
                </div>
            </section>

            <!-- Recommended Sites -->
            <section class="container-fluid py-5 mt-5 site-section-container">
                <div class="d-flex justify-content-between align-items-end mb-4 px-4">
                    <div>
                        <h2 class="h1 fw-black text-dark tracking-tight mb-2">추천 캠핑 사이트</h2>
                        <p class="text-secondary fw-medium m-0 fs-5">당신에게 어울리는 완벽한 공간을 찾아보세요</p>
                    </div>
                    <button class="btn btn-link text-primary fw-bold text-decoration-none d-flex align-items-center gap-1 p-0 fs-6">
                        전체보기 <span class="material-symbols-outlined fs-6">open_in_new</span>
                    </button>
                </div>

                <div class="row g-4 px-4">
                    <c:forEach items="${sites}" var="site">
                    <!-- Site Card -->
                    <div class="col-sm-6 col-lg-3">
                        <div class="fh-card p-0 border-0 shadow-sm overflow-hidden h-100">
                            <%-- TODO: 이미지 도메인 연동 시 실제 경로로 변경 --%>
                            <img src="/images/camping_review2.jpg" alt="${site.siteName}" class="w-100 object-fit-cover site-card-img">
                            <div class="p-4">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <h3 class="h5 fw-bold text-dark m-0">${site.siteName}</h3>
                                    <div class="d-flex align-items-center text-primary fw-bold">
                                        <span class="material-symbols-outlined fs-5 me-1 icon-fill-1">star</span> ${site.rating}
                                    </div>
                                </div>
                                <p class="text-secondary small fw-medium mb-4">${site.zoneName} 구역 사이트</p>
                                <div class="pt-3 border-top d-flex justify-content-between align-items-center">
                                    <span class="h5 fw-black text-primary m-0">₩${site.price} ~</span>
                                    <button onclick="location.href='/sites/${site.id}'" class="btn btn-primary btn-sm px-3 fw-bold shadow-sm">상세보기</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    </c:forEach>
                </div>
            </section>
        </main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
