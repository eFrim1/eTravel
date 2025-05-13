export default class Review {
  constructor({ id, clientId, rating, comment, createdAt }) {
    this.id = id;
    this.clientId = clientId;
    this.rating = rating;
    this.comment = comment;
    this.createdAt = createdAt;
  }
} 