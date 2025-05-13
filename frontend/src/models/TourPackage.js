export default class TourPackage {
  constructor({ id, destination, price, startDate, endDate, images, reviews }) {
    this.id = id;
    this.destination = destination;
    this.price = price;
    this.startDate = startDate;
    this.endDate = endDate;
    this.images = images;
    this.reviews = reviews;
  }
}
