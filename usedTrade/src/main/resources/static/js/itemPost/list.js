document.querySelectorAll(".sort-btn").forEach(btn => { // “이 문서에서 .sort-btn 클래스가 붙은 모든 요소를 찾아서,
                                                        //   하나씩 꺼내서(btn) 안에 있는 코드를 실행해라”
    btn.addEventListener("click", e => {
        e.preventDefault(); // 이 요소가 원래 하려던 기본 행동(페이지 이동, 폼 제출 등)을 막아라

        const sort = btn.dataset.sort;
        loadList(sort);
    });
});

function loadList(sort) {
    fetch(`/item-posts?sort=${sort}`, {
        headers: {
            "X-Requested-With": "XMLHttpRequest"
        }
    })
    .then(res => res.text())
    .then(html => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, "text/html");

        const newGrid = doc.querySelector(".grid-container");
        document.querySelector(".grid-container").innerHTML = newGrid.innerHTML;
    });
}