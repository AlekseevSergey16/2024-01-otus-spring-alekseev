const fetchAuthors = () => fetch('/api/authors')
    .then(resp => resp.json());

const fetchGenres = () => fetch('/api/genres')
    .then(resp => resp.json());

const fetchBookById = bookId => fetch('/api/books/' + bookId)
    .then(resp => resp.json());

const createBook = book => fetch('/api/books', {
    method: 'post',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(book)
});

const updateBook = (bookId, book) => fetch('/api/books/' + bookId, {
    method: 'put',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(book)
});

const deleteBookById = bookId =>
    fetch('/api/books/' + bookId, { method: 'delete' });



