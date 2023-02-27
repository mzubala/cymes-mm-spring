package pl.com.bottega.cymes.commons.test;

import com.github.javafaker.Address;
import com.github.javafaker.Ancient;
import com.github.javafaker.Animal;
import com.github.javafaker.App;
import com.github.javafaker.AquaTeenHungerForce;
import com.github.javafaker.Artist;
import com.github.javafaker.Avatar;
import com.github.javafaker.Aviation;
import com.github.javafaker.BackToTheFuture;
import com.github.javafaker.Beer;
import com.github.javafaker.Book;
import com.github.javafaker.Bool;
import com.github.javafaker.Buffy;
import com.github.javafaker.Business;
import com.github.javafaker.Cat;
import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Code;
import com.github.javafaker.Color;
import com.github.javafaker.Commerce;
import com.github.javafaker.Company;
import com.github.javafaker.Country;
import com.github.javafaker.Crypto;
import com.github.javafaker.Currency;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Demographic;
import com.github.javafaker.Dog;
import com.github.javafaker.DragonBall;
import com.github.javafaker.Dune;
import com.github.javafaker.Educator;
import com.github.javafaker.ElderScrolls;
import com.github.javafaker.Esports;
import com.github.javafaker.File;
import com.github.javafaker.Finance;
import com.github.javafaker.Food;
import com.github.javafaker.Friends;
import com.github.javafaker.FunnyName;
import com.github.javafaker.GameOfThrones;
import com.github.javafaker.Hacker;
import com.github.javafaker.HarryPotter;
import com.github.javafaker.Hipster;
import com.github.javafaker.HitchhikersGuideToTheGalaxy;
import com.github.javafaker.Hobbit;
import com.github.javafaker.HowIMetYourMother;
import com.github.javafaker.IdNumber;
import com.github.javafaker.Internet;
import com.github.javafaker.Job;
import com.github.javafaker.LeagueOfLegends;
import com.github.javafaker.Lebowski;
import com.github.javafaker.LordOfTheRings;
import com.github.javafaker.Lorem;
import com.github.javafaker.Matz;
import com.github.javafaker.Medical;
import com.github.javafaker.Music;
import com.github.javafaker.Name;
import com.github.javafaker.Nation;
import com.github.javafaker.Number;
import com.github.javafaker.Options;
import com.github.javafaker.Overwatch;
import com.github.javafaker.PhoneNumber;
import com.github.javafaker.Pokemon;
import com.github.javafaker.PrincessBride;
import com.github.javafaker.ProgrammingLanguage;
import com.github.javafaker.Relationships;
import com.github.javafaker.RickAndMorty;
import com.github.javafaker.Robin;
import com.github.javafaker.RockBand;
import com.github.javafaker.Shakespeare;
import com.github.javafaker.SlackEmoji;
import com.github.javafaker.Space;
import com.github.javafaker.StarTrek;
import com.github.javafaker.Stock;
import com.github.javafaker.Superhero;
import com.github.javafaker.Team;
import com.github.javafaker.TwinPeaks;
import com.github.javafaker.University;
import com.github.javafaker.Weather;
import com.github.javafaker.Witcher;
import com.github.javafaker.Yoda;
import com.github.javafaker.Zelda;
import com.github.javafaker.service.RandomService;
import org.springframework.stereotype.Component;

@Component
public class Faker {

    private final com.github.javafaker.Faker faker = com.github.javafaker.Faker.instance();

    public String numerify(String numberString) {
        return faker.numerify(numberString);
    }

    public String letterify(String letterString) {
        return faker.letterify(letterString);
    }

    public String letterify(String letterString, boolean isUpper) {
        return faker.letterify(letterString, isUpper);
    }

    public String bothify(String string) {
        return faker.bothify(string);
    }

    public String bothify(String string, boolean isUpper) {
        return faker.bothify(string, isUpper);
    }

    public String regexify(String regex) {
        return faker.regexify(regex);
    }

    public RandomService random() {
        return faker.random();
    }

    public Currency currency() {
        return faker.currency();
    }

    public Ancient ancient() {
        return faker.ancient();
    }

    public App app() {
        return faker.app();
    }

    public Artist artist() {
        return faker.artist();
    }

    public Avatar avatar() {
        return faker.avatar();
    }

    public Aviation aviation() {
        return faker.aviation();
    }

    public Music music() {
        return faker.music();
    }

    public Name name() {
        return faker.name();
    }

    public Number number() {
        return faker.number();
    }

    public Internet internet() {
        return faker.internet();
    }

    public PhoneNumber phoneNumber() {
        return faker.phoneNumber();
    }

    public Pokemon pokemon() {
        return faker.pokemon();
    }

    public Lorem lorem() {
        return faker.lorem();
    }

    public Address address() {
        return faker.address();
    }

    public Book book() {
        return faker.book();
    }

    public Buffy buffy() {
        return faker.buffy();
    }

    public Business business() {
        return faker.business();
    }

    public ChuckNorris chuckNorris() {
        return faker.chuckNorris();
    }

    public Color color() {
        return faker.color();
    }

    public Commerce commerce() {
        return faker.commerce();
    }

    public Company company() {
        return faker.company();
    }

    public Crypto crypto() {
        return faker.crypto();
    }

    public Hacker hacker() {
        return faker.hacker();
    }

    public IdNumber idNumber() {
        return faker.idNumber();
    }

    public Options options() {
        return faker.options();
    }

    public Code code() {
        return faker.code();
    }

    public File file() {
        return faker.file();
    }

    public Finance finance() {
        return faker.finance();
    }

    public Food food() {
        return faker.food();
    }

    public ElderScrolls elderScrolls() {
        return faker.elderScrolls();
    }

    public GameOfThrones gameOfThrones() {
        return faker.gameOfThrones();
    }

    public DateAndTime date() {
        return faker.date();
    }

    public Demographic demographic() {
        return faker.demographic();
    }

    public Dog dog() {
        return faker.dog();
    }

    public Educator educator() {
        return faker.educator();
    }

    public SlackEmoji slackEmoji() {
        return faker.slackEmoji();
    }

    public Shakespeare shakespeare() {
        return faker.shakespeare();
    }

    public Space space() {
        return faker.space();
    }

    public Superhero superhero() {
        return faker.superhero();
    }

    public Bool bool() {
        return faker.bool();
    }

    public Team team() {
        return faker.team();
    }

    public Beer beer() {
        return faker.beer();
    }

    public University university() {
        return faker.university();
    }

    public Cat cat() {
        return faker.cat();
    }

    public Stock stock() {
        return faker.stock();
    }

    public LordOfTheRings lordOfTheRings() {
        return faker.lordOfTheRings();
    }

    public Zelda zelda() {
        return faker.zelda();
    }

    public HarryPotter harryPotter() {
        return faker.harryPotter();
    }

    public RockBand rockBand() {
        return faker.rockBand();
    }

    public Esports esports() {
        return faker.esports();
    }

    public Friends friends() {
        return faker.friends();
    }

    public Hipster hipster() {
        return faker.hipster();
    }

    public Job job() {
        return faker.job();
    }

    public TwinPeaks twinPeaks() {
        return faker.twinPeaks();
    }

    public RickAndMorty rickAndMorty() {
        return faker.rickAndMorty();
    }

    public Yoda yoda() {
        return faker.yoda();
    }

    public Matz matz() {
        return faker.matz();
    }

    public Witcher witcher() {
        return faker.witcher();
    }

    public DragonBall dragonBall() {
        return faker.dragonBall();
    }

    public FunnyName funnyName() {
        return faker.funnyName();
    }

    public HitchhikersGuideToTheGalaxy hitchhikersGuideToTheGalaxy() {
        return faker.hitchhikersGuideToTheGalaxy();
    }

    public Hobbit hobbit() {
        return faker.hobbit();
    }

    public HowIMetYourMother howIMetYourMother() {
        return faker.howIMetYourMother();
    }

    public LeagueOfLegends leagueOfLegends() {
        return faker.leagueOfLegends();
    }

    public Overwatch overwatch() {
        return faker.overwatch();
    }

    public Robin robin() {
        return faker.robin();
    }

    public StarTrek starTrek() {
        return faker.starTrek();
    }

    public Weather weather() {
        return faker.weather();
    }

    public Lebowski lebowski() {
        return faker.lebowski();
    }

    public Medical medical() {
        return faker.medical();
    }

    public Country country() {
        return faker.country();
    }

    public Animal animal() {
        return faker.animal();
    }

    public BackToTheFuture backToTheFuture() {
        return faker.backToTheFuture();
    }

    public PrincessBride princessBride() {
        return faker.princessBride();
    }

    public Relationships relationships() {
        return faker.relationships();
    }

    public Nation nation() {
        return faker.nation();
    }

    public Dune dune() {
        return faker.dune();
    }

    public AquaTeenHungerForce aquaTeenHungerForce() {
        return faker.aquaTeenHungerForce();
    }

    public ProgrammingLanguage programmingLanguage() {
        return faker.programmingLanguage();
    }

    public String resolve(String key) {
        return faker.resolve(key);
    }

    public String expression(String expression) {
        return faker.expression(expression);
    }
}
